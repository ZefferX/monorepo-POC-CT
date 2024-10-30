package poc.custom.token.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import poc.custom.token.filter.JwtPermissionFilter;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<JwtPermissionFilter> jwtPermissionFilter() {
        FilterRegistrationBean<JwtPermissionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtPermissionFilter());
        registrationBean.addUrlPatterns("/test/*"); // Aplica el filtro a los endpoints que comiencen con /api/
        return registrationBean;
    }
}
