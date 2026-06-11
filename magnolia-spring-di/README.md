# Magnolia Spring DI

Provides a bridge between Spring DI and Magnolia/Guice. Collection bindings are not supported / have to be bridged manually.

## Requirements
* Java 17
* Magnolia >= 6.4

## Setup

### Add Maven dependency:
```xml
<dependency>
    <groupId>com.merkle.oss.magnolia</groupId>
    <artifactId>magnolia-spring-di</artifactId>
    <version>0.1.5</version>
</dependency>
```

Add `MagnoliaSpringConfiguration` to your configuration:

```java
import org.springframework.context.annotation.Import;
import com.merkle.oss.magnolia.di.config.MagnoliaSpringConfiguration;

@Import({
        MagnoliaSpringConfiguration.class
})
public class SomeConfiguration {}
```

### Spring -> Guice
Spring beans can be injected in `Components.getComponent/newInstance` without any additional configuration.

### Guice -> Spring
Set the [SpringBridgeAnnotationConfigWebApplicationContext](src/main/java/com/merkle/oss/magnolia/di/bridge/SpringBridgeAnnotationConfigWebApplicationContext.java) on your DispatcherServlet(s).


