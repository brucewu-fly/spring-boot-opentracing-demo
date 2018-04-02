# spring-boot-opentracing-demo

## 编译运行 demo
```
git clone https://github.com/brucewu-fly/spring-boot-opentracing-demo.git
cd spring-boot-opentracing-demo
mvn clean package
export PROJECT=<your_project> ENDPOINT=<your_endpoint> ACCESS_KEY_ID=<your_access_key_id> ACCESS_KEY_SECRET=<your_access_key_secret> LOG_STORE=<your_log_store> && java -jar target/spring-boot-opentracing-demo-1.0-SNAPSHOT.jar
curl http://localhost:8080/hello
curl http://localhost:8080/chaining
```

