package com.linksphere.backend.AllConfigurations;

import com.linksphere.backend.AuthenticationFilter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AuthenticationConfiguration {
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> customAuthenticationFilter(AuthenticationFilter filter) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}