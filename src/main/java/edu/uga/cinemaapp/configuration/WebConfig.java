// package edu.uga.cinemaapp.configuration;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// @Configuration
// @EnableWebMvc
// public class WebConfig extends WebMvcConfigurerAdapter {

//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         registry.addResourceHandler("/webjars/**", "/images/**", "/css/**", "/js/**").addResourceLocations(
//                 "classpath:/META-INF/resources/webjars/", "classpath:/static/images/", "classpath:/static/css/",
//                 "classpath:/static/js/");
//     }

// }