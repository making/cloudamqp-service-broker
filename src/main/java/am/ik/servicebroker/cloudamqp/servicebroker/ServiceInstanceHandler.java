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
import java.util.Map;

import static am.ik.servicebroker.cloudamqp.client.CloudAmqpPlan.LEMUR;
import static am.ik.servicebroker.cloudamqp.client.CloudAmqpRegion.AMAZON_WEB_SERVICES_AP_NORTHEAST_1;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class ServiceInstanceHandler {
    private static final Logger log = LoggerFactory.getLogger(ServiceInstanceHandler.class);
    private final CloudAmqpClient cloudAmqpClient;

    public ServiceInstanceHandler(CloudAmqpClient cloudAmqpClient) {
        this.cloudAmqpClient = cloudAmqpClient;
    }

    public RouterFunction<ServerResponse> routes() {
        return nest(path("/v2/service_instances/{instanceId}"),
                route(PUT("/"), this::provisioning)
                        .andRoute(PATCH("/"), this::update)
                        .andRoute(DELETE("/"), this::deprovisioning));
    }

    Mono<ServerResponse> provisioning(ServerRequest request) {
        String instanceId = request.pathVariable("instanceId");
        log.info("Provisioning instanceId={}", instanceId);
        String name = "cf_" + instanceId;
        Mono<CloudAmqpInstance> instance =
                this.cloudAmqpClient.createInstance(name, LEMUR, AMAZON_WEB_SERVICES_AP_NORTHEAST_1)
                        .flatMap(i -> this.cloudAmqpClient.instanceDetail(i.getId()));
        return status(HttpStatus.CREATED)
                .body(instance.map(ProvisioningResponse::new), ProvisioningResponse.class);
    }

    Mono<ServerResponse> update(ServerRequest request) {
        return ok().syncBody(Collections.emptyMap());
    }

    Mono<ServerResponse> deprovisioning(ServerRequest request) {
        String instanceId = request.pathVariable("instanceId");
        log.info("Deprovisioning instanceId={}", instanceId);
        String name = "cf_" + instanceId;
        Mono<Void> then = this.cloudAmqpClient.listInstances()
                .filter(i -> name.equals(i.getName()))
                .map(CloudAmqpInstance::getId)
                .flatMap(this.cloudAmqpClient::deleteInstance)
                .then();
        Map<Object, Object> body = Collections.emptyMap();
        return then
                .flatMap(v -> ok().syncBody(body))
                .switchIfEmpty(status(HttpStatus.GONE).syncBody(body));
    }

    public static class ProvisioningResponse {
        public String dashboard_url;

        public ProvisioningResponse(CloudAmqpInstance instance) {
            this.dashboard_url = instance.dashboardUrl();
        }
    }
}
