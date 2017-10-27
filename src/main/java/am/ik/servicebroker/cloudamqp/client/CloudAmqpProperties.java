package am.ik.servicebroker.cloudamqp.client;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "service-broker.cloudamqp")
@Component
@Validated
public class CloudAmqpProperties {
    @URL
    private String apiUrl = "https://customer.cloudamqp.com/api/";
    @NotEmpty
    private String apiKey;
    @NotNull
    private CloudAmqpRegion defaultRegion = CloudAmqpRegion.AMAZON_WEB_SERVICES_AP_NORTHEAST_1;
    @NotNull
    private CloudAmqpPlan defaultPlan = CloudAmqpPlan.LEMUR;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public CloudAmqpRegion getDefaultRegion() {
        return defaultRegion;
    }

    public void setDefaultRegion(CloudAmqpRegion defaultRegion) {
        this.defaultRegion = defaultRegion;
    }

    public CloudAmqpPlan getDefaultPlan() {
        return defaultPlan;
    }

    public void setDefaultPlan(CloudAmqpPlan defaultPlan) {
        this.defaultPlan = defaultPlan;
    }
}
