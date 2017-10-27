package am.ik.servicebroker.cloudamqp.client;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CloudAmqpClient {
    private final WebClient webClient;

    public CloudAmqpClient(WebClient.Builder builder, CloudAmqpProperties props) {
        String basic = Base64Utils.encodeToString((props.getApiKey() + ":").getBytes());
        this.webClient = builder
                .baseUrl(props.getApiUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                .build();
    }

    public Flux<CloudAmqpInstance> listInstances() {
        return this.webClient.get()
                .uri("instances")
                .retrieve()
                .bodyToFlux(CloudAmqpInstance.class);
    }


    public Mono<CloudAmqpInstance> instanceDetail(String id) {
        return this.webClient.get()
                .uri("instances/{id}", id)
                .retrieve()
                .bodyToMono(CloudAmqpInstance.class);
    }

    public Mono<CloudAmqpInstance> createInstance(String name, CloudAmqpPlan plan, CloudAmqpRegion region) {
        return this.webClient.post()
                .uri(b -> b.pathSegment("instances")
                        .queryParam("name", name)
                        .queryParam("plan", plan.toString())
                        .queryParam("region", region.toString())
                        .build())
                .retrieve()
                .bodyToMono(CloudAmqpInstance.class);
    }

    public Mono<Void> deleteInstance(String id) {
        return this.webClient.delete()
                .uri("instances/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
