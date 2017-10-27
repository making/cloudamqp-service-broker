package am.ik.servicebroker.cloudamqp.servicebroker;

import am.ik.servicebroker.cloudamqp.client.CloudAmqpClient;
import am.ik.servicebroker.cloudamqp.client.CloudAmqpInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class ServiceInstanceServiceBindingHandler {
    private static final Logger log = LoggerFactory.getLogger(ServiceInstanceServiceBindingHandler.class);
    private final CloudAmqpClient cloudAmqpClient;

    public ServiceInstanceServiceBindingHandler(CloudAmqpClient cloudAmqpClient) {
        this.cloudAmqpClient = cloudAmqpClient;
    }

    public RouterFunction<ServerResponse> routes() {
        return nest(path("/v2/service_instances/{instanceId}/service_bindings/{bindingId}"),
                route(PUT("/"), this::bind)
                        .andRoute(DELETE("/"), this::unbind));
    }

    Mono<ServerResponse> bind(ServerRequest request) {
        String instanceId = request.pathVariable("instanceId");
        String bindingId = request.pathVariable("bindingId");
        log.info("bind instanceId={}, bindingId={}", instanceId, bindingId);
        String name = "cf_" + instanceId;
        Mono<CloudAmqpInstance> instance = this.cloudAmqpClient.listInstances()
                .filter(i -> name.equals(i.getName()))
                .next()
                .map(CloudAmqpInstance::getId)
                .flatMap(this.cloudAmqpClient::instanceDetail);
        return instance.map(CloudAmqpInstance::credentials)
                .flatMap(c -> status(HttpStatus.CREATED).syncBody(Collections.singletonMap("credentials", c)))
                .switchIfEmpty(status(HttpStatus.GONE).syncBody(Collections.emptyMap()));
    }

    Mono<ServerResponse> unbind(ServerRequest request) {
        return ok().syncBody(Collections.emptyMap());
    }
}
