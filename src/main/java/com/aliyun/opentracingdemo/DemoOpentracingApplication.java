package com.aliyun.opentracingdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.uber.jaeger.Configuration;
import com.uber.jaeger.samplers.ProbabilisticSampler;

@SpringBootApplication
public class DemoOpentracingApplication {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public io.opentracing.Tracer jaegerTracer() {
        Configuration.SamplerConfiguration samplerConfig =
                new Configuration.SamplerConfiguration().
                        withType(ProbabilisticSampler.TYPE).
                        withParam(1);
        return new Configuration("spring-boot-demo").
                withSampler(samplerConfig).
                withReporter(new Configuration.ReporterConfiguration()).
                getTracer();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoOpentracingApplication.class, args);
    }
}
