package am.ik.servicebroker.cloudamqp.config;

//import org.springframework.boot.actuate.autoconfigure.security.EndpointRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.HttpSecurity;
import org.springframework.security.core.userdetails.MapUserDetailsRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    private final ServiceBrokerAdmin admin;

    public SecurityConfig(ServiceBrokerAdmin admin) {
        this.admin = admin;
    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeExchange()
                .pathMatchers("/v2/**").hasRole("ADMIN") //
                .pathMatchers("/application/**").hasRole("ADMIN") //
                .and()
                .httpBasic()
                .and()
                .build();
    }

    @Bean
    public MapUserDetailsRepository userDetailsRepository() {
        return new MapUserDetailsRepository(this.admin);
    }
}
