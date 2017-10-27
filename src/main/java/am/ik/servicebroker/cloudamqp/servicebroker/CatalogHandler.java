package am.ik.servicebroker.cloudamqp.servicebroker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class CatalogHandler {
    private final Object catalog;

    public CatalogHandler(@Value("${service-broker.catalog:classpath:catalog.yml}") Resource catalog) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream stream = catalog.getInputStream()) {
            this.catalog = yaml.load(stream);
        }
    }

    public RouterFunction<ServerResponse> routes() {
        return route(GET("/v2/catalog"), this::catalog);
    }

    Mono<ServerResponse> catalog(ServerRequest request) {
        return ServerResponse.ok().syncBody(this.catalog);
    }
}
