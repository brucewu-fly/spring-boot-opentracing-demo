# spring-boot-opentracing-demo

## 编译运行 demo
```
mvn clean package
java -jar target/spring-boot-opentracing-demo-1.0-SNAPSHOT.jar
curl http://localhost:8080/hello
curl http://localhost:8080/chaining
```

## 运行 jaeger
参阅 https://github.com/aliyun/aliyun-log-jaeger/blob/master/README_CN.md

