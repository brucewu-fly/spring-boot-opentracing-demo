# spring-boot-opentracing-demo

## 编译运行 demo
```
git clone https://github.com/brucewu-fly/spring-boot-opentracing-demo.git
cd spring-boot-opentracing-demo
mvn clean package

## 运行实例
export PROJECT=<your_project> ENDPOINT=<your_endpoint> ACCESS_KEY_ID=<your_access_key_id> ACCESS_KEY_SECRET=<your_access_key_secret> LOG_STORE=<your_log_store> && java -jar target/spring-boot-opentracing-demo-1.0-SNAPSHOT.jar

## 向下列 endpoint 发送请求
curl http://localhost:8080/hello
curl http://localhost:8080/chaining
```

## 实例详解
该样例使用 [aliyun-log-jaeger-sender](https://github.com/aliyun/aliyun-log-jaeger-sender) 将追踪数据发往日志服务。

### 导入依赖
pom.xml 导入对 opentracing-spring-cloud， opentracing-api，aliyun-log-jaeger-sender 的依赖。
```
<dependency>
	<groupId>io.opentracing.contrib</groupId>
	<artifactId>opentracing-spring-cloud-starter</artifactId>
	<version>0.1.0</version>
</dependency>

<dependency>
	<groupId>io.opentracing</groupId>
	<artifactId>opentracing-api</artifactId>
	<version>0.31.0</version>
</dependency>

<dependency>
    <groupId>com.aliyun.openservices</groupId>
    <artifactId>aliyun-log-jaeger-sender</artifactId>
    <version>0.0.3</version>
</dependency>
```

### 实例化 tracer 实现
这里选择 jaeger 作为 tracing 的实现。
```java
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
```

### 业务逻辑
这里业务逻辑部分不需要附加任何 tracing 相关的代码，tracing 数据将会被收集。
```java
@RestController
public class HelloController {

  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping("/hello")
  public String hello() {
      return "Hello from Spring Boot!";
  }

  @RequestMapping("/chaining")
  public String chaining() {
      ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/hello", String.class);
      return "Chaining + " + response.getBody();
  }
}
```
