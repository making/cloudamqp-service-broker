package am.ik.servicebroker.cloudamqp.config;

import am.ik.servicebroker.cloudamqp.servicebroker.CatalogHandler;
import am.ik.servicebroker.cloudamqp.servicebroker.ServiceInstanceHandler;
import am.ik.servicebroker.cloudamqp.servicebroker.ServiceInstanceServiceBindingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> routes(CatalogHandler catalogHandler,
                                                 ServiceInstanceHandler serviceInstanceHandler,
                                                 ServiceInstanceServiceBindingHandler serviceInstanceServiceBindingHandler) {
        return catalogHandler.routes()
                .and(serviceInstanceHandler.routes())
                .and(serviceInstanceServiceBindingHandler.routes());
    }
}
