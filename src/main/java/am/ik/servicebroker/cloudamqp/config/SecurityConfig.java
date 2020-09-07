package am.ik.servicebroker.cloudamqp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    private final ServiceBrokerAdmin admin;

    public SecurityConfig(ServiceBrokerAdmin admin) {
        this.admin = admin;
    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
				.authorizeExchange()
                .pathMatchers("/v2/**").hasRole("ADMIN") //
                .pathMatchers("/application/**").hasRole("ADMIN")
				.and()
				.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsRepository() {
        return new MapReactiveUserDetailsService(this.admin);
    }
}
