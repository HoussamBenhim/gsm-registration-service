package com.registeration.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerClientRequestTransformer;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;


@SpringBootApplication
//@EnableDiscoveryClient
@EnableAsync
public class RegistrationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistrationServiceApplication.class, args);
    }

    static Flux<Student> call(WebClient http, String url) {
        return http.get().uri(url).retrieve().bodyToFlux(Student.class);
    }
}

@Component @Log4j2
class  ConfiguredWebClientRunner{
    ConfiguredWebClientRunner(WebClient http) {
        RegistrationServiceApplication.call(http, "http://studentservice/v1/student/1").subscribe(student->log.info("configured : "+ student.getFirstname()));
    }
}

@Component
@Log4j2
class WebClientRunner {

    WebClientRunner(ReactiveLoadBalancer.Factory<ServiceInstance> serviceInstanceFactory, List<LoadBalancerClientRequestTransformer> transformers) {
        var filter = new ReactorLoadBalancerExchangeFilterFunction(serviceInstanceFactory, transformers);

        var http = WebClient.builder()
                .filter(filter)
                .build();

        RegistrationServiceApplication.call(http, "http://studentservice/v1/student/1").subscribe(greeting -> log.info("filter: " + greeting.toString()));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Student {
    private Long id;
    private String firstname;
    private String lastname;
}



@Component
@Slf4j
class ReactiveLoadBalancerFactoryRunner {
    public ReactiveLoadBalancerFactoryRunner(ReactiveLoadBalancer.Factory<ServiceInstance> serviceInstanceFactory) {
        var http = WebClient.builder().build();
        ReactiveLoadBalancer<ServiceInstance> instance = serviceInstanceFactory.getInstance("studentservice");
        Flux<Response<ServiceInstance>> chosen = Flux.from(instance.choose());
        chosen.map(responseServerInstance -> {
                    ServiceInstance server = responseServerInstance.getServer();
                    var url = "http://" + server.getHost() + ":" + server.getPort() + "/v1/student/1";
                    log.info(url);
                    return url;
                })
                .flatMap(url -> RegistrationServiceApplication.call(http, url))
                .subscribe(student -> log.info("manuel " + student.getFirstname()));
    }
}