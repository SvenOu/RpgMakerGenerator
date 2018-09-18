package com.rmv.code.generator.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * like web.xml
 */
@Configuration
@PropertySource(value = {
        "classpath:application.properties",
        "classpath:image-tools.properties"
})
public class Web extends WebMvcConfigurerAdapter {
    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {

        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                //welcome page
                registry.addViewController("/").setViewName("redirect:/index.html");
                //error pages
//                registry.addViewController("/302").setViewName("forward:/login.html");
//                registry.addViewController("/304").setViewName("forward:/login.html");
//                registry.addViewController("/403").setViewName("forward:/login.html");
//                registry.addViewController("/404").setViewName("forward:/login.html");
//                registry.addViewController("/500").setViewName("forward:/login.html");
            }
        };
    }
}
