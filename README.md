# Magnolia SpringRenderer

The spring renderer module makes the Spring-Framework available for Magnolia. <bR>
Recommended to be used with [dynamic builders modules](https://github.com/merkle-open/magnolia-dynamic-builders). 

## Requirements
* Java 17
* Magnolia >= 6.3

## Setup

### Add Maven dependency:
```xml
<dependency>
    <groupId>com.merkle.oss.magnolia</groupId>
    <artifactId>magnolia-spring-renderer</artifactId>
    <version>0.0.2</version>
</dependency>
```

### Spring dispatcher-servlet
Create config:
```java
import info.magnolia.rendering.renderer.FreemarkerRenderer;

import java.util.List;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.merkle.oss.magnolia.renderer.spring.MagnoliaHandlerMethodArgumentResolver;
import com.merkle.oss.magnolia.renderer.spring.MagnoliaTemplateView;
import com.merkle.oss.magnolia.renderer.spring.cache.DisableCacheHandlerInterceptorAdapter;

@Configuration
public class SpringRendererServletConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new MagnoliaHandlerMethodArgumentResolver());
    }

    @Override
    protected void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new DisableCacheHandlerInterceptorAdapter());
    }

    @Bean
    public MagnoliaTemplateView.Resolver viewResolver() {
        return new MagnoliaTemplateView.Resolver(FreemarkerRenderer.class);
    }
    
    ...
}
```

Create DispatcherServletProvider:
```java
import info.magnolia.cms.util.CustomServletConfig;
import info.magnolia.objectfactory.Components;

import java.util.Collections;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.machinezoo.noexception.Exceptions;
import com.merkle.oss.magnolia.renderer.spring.DispatcherServletProvider;

public class SpringRendererDispatcherServletProvider implements DispatcherServletProvider {
    private final DispatcherServlet dispatcherServlet;

    @Inject
    public SpringRendererDispatcherServletProvider() {
        dispatcherServlet = Components.newInstance(DispatcherServlet.class);
        dispatcherServlet.setContextConfigLocation(SpringRendererServletConfiguration.class.getName());
        dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
        Exceptions.wrap().run(() -> dispatcherServlet.init(new CustomServletConfig("springRenderer", Components.getComponent(ServletContext.class), Collections.emptyMap())));
    }

    @Override
    public DispatcherServlet get() {
        return dispatcherServlet;
    }
}
```

Add guice binding:
```xml
<component>
    <type>com.merkle.oss.magnolia.renderer.spring.DispatcherServletProvider</type>
    <implementation>com.somepackage.SpringRendererDispatcherServletProvider</implementation>
    <scope>singleton</scope>
</component>
```

## How to use
Create a spring controller with one requestHandler method that matches the id of the template.

## Example
### Component-template

```java
import javax.jcr.Node;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.merkle.oss.magnolia.renderer.spring.SpringRenderer;
import com.merkle.oss.magnolia.templatebuilder.annotation.Template;

@Template(
        id = SomeComponent.ID,
        title = "templates.components." + SomeComponent.NAME + ".title",
        dialog = SomeComponentDialog.ID,
        description = "templates.components." + SomeComponent.NAME + ".description",
        renderer = SpringRenderer.NAME
        //templateScript is provided dynamically in render method below!!
)
@Controller
public class SomeComponent extends BaseComponent {
    public static final String NAME = "SomeComponent";
    public static final String ID = "SomeApp:components/" + NAME;

    @RequestMapping(ID)
    public String render(final Model model, final Node node) {
        model.addAttribute("someComponentAttribute", "someValue");
        return "/someModule/templates/components/somePage.ftl";
    }
}
```
### Page-template with area

```java
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.jcr.Node;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.merkle.oss.magnolia.templatebuilder.annotation.Available;
import com.merkle.oss.magnolia.templatebuilder.annotation.Template;
import com.merkle.oss.magnolia.templatebuilder.annotation.area.Area;
import com.merkle.oss.magnolia.templatebuilder.annotation.area.AvailableComponentClasses;
import com.merkle.oss.magnolia.templatebuilder.annotation.area.AvailableComponents;

@Template(
        id = SomePage.ID,
        title = "templates.pages." + SomePage.NAME + ".title",
        dialog = SomePageDialog.ID,
        description = "templates.pages." + SomePage.NAME + ".description",
        renderer = "freemarker"
        //templateScript is provided dynamically in render method below!!
)
@Controller
public class SomePage {
    public static final String NAME = "SomePage";
    public static final String ID = "SomeApp:pages/" + NAME;

    @Available
    public boolean isAvailable(final Node node) {
        //TODO implement
        return true;
    }

    @RequestMapping(ID)
    public String render(final Model model, final Node node) {
        model.addAttribute("somePageAttribute", "someValue");
        return "/someModule/templates/pages/somePage.ftl";
    }

    @Area(
            id = ContentArea.ID,
            name = ContentArea.NAME,
            title = "templates.areas." + SomePage.ContentArea.NAME + ".title"
            //templateScript is provided dynamically in render method below!!
    )
    @AvailableComponentClasses({ SomeComponent.class })
    @Controller
    public static class ContentArea {
        public static final String NAME = "ContentArea";
        public static final String ID = SomePage.ID + "/" + NAME;

        @RequestMapping(ID)
        public String render(final Model model, final Node node) {
            model.addAttribute("someAreaAttribute", "someValue");
            return "/someModule/templates/areas/contentArea.ftl";
        }
    }
}
```

## Customization
### HandlerMethodArgumentResolver
Implement and bind a different HandlerMethodArgumentResolver if necessary (see spring config).