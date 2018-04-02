package com.aliyun.opentracingdemo;

import com.aliyun.openservices.log.jaeger.sender.AliyunLogSender;
import com.uber.jaeger.Tracer;
import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.samplers.ConstSampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class DemoOpentracingApplication {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  private AliyunLogSender buildAliyunLogSender() {
    String projectName = System.getenv("PROJECT");
    String logStore = System.getenv("LOG_STORE");
    String endpoint = System.getenv("ENDPOINT");
    String accessKeyId = System.getenv("ACCESS_KEY_ID");
    String accessKey = System.getenv("ACCESS_KEY_SECRET");
    return new AliyunLogSender.Builder(projectName, logStore, endpoint, accessKeyId, accessKey)
        .withTopic("test_topic").build();
  }

  @Bean
  public io.opentracing.Tracer jaegerTracer() {
    AliyunLogSender aliyunLogSender = buildAliyunLogSender();
    RemoteReporter remoteReporter = new RemoteReporter.Builder()
        .withSender(aliyunLogSender)
        .build();
    return new Tracer.Builder("spring-boot-demo")
        .withReporter(remoteReporter)
        .withSampler(new ConstSampler(true))
        .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoOpentracingApplication.class, args);
  }
}
