package am.ik.servicebroker.cloudamqp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CloudAmqpInstance implements Serializable {
    private final String id;
    private final CloudAmqpPlan plan;
    private final CloudAmqpRegion region;
    private final String name;
    private final String url;

    @JsonCreator
    public CloudAmqpInstance(
            @JsonProperty("id") String id,
            @JsonProperty("plan") CloudAmqpPlan plan,
            @JsonProperty("region") CloudAmqpRegion region,
            @JsonProperty("name") String name,
            @JsonProperty("url") String url) {
        this.id = id;
        this.plan = plan;
        this.region = region;
        this.name = name;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public CloudAmqpPlan getPlan() {
        return plan;
    }

    public CloudAmqpRegion getRegion() {
        return region;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String dashboardUrl() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.url);
        String[] userInfo = builder.build().getUserInfo().split(":");
        return builder.cloneBuilder()
                .scheme("https")
                .userInfo(null)
                .replacePath(null)
                .toUriString() + "/#/login/" + userInfo[0] + "/" + userInfo[1];
    }

    public Map<String, Object> credentials() {
        Map<String, Object> credentials = new LinkedHashMap<>();
        UriComponents build = UriComponentsBuilder.fromUriString(this.url).build();
        String[] userInfo = build.getUserInfo().split(":");
        credentials.put("dashboard_url", this.dashboardUrl());
        credentials.put("hostname", build.getHost());
        credentials.put("username", userInfo[0]);
        credentials.put("password", userInfo[1]);
        credentials.put("port", build.getPort() == -1 ? 5672 : build.getPort());
        credentials.put("vhost", build.getPath().replace("/", ""));
        credentials.put("uri", this.url);
        return Collections.unmodifiableMap(credentials);
    }

    @Override
    public String toString() {
        return "CloudAmqpInstance{" +
                "id='" + id + '\'' +
                ", plan='" + plan + '\'' +
                ", region='" + region + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
