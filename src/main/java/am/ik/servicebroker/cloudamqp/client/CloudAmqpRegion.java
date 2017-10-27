package am.ik.servicebroker.cloudamqp.client;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum CloudAmqpRegion {
    AMAZON_WEB_SERVICES_AP_NORTHEAST_1("amazon-web-services::ap-northeast-1");

    private final String value;

    CloudAmqpRegion(String value) {
        this.value = value;
    }

    @JsonCreator
    public static CloudAmqpRegion of(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equals(value))
                .findAny()
                .get();
    }

    @Override
    public String toString() {
        return this.value;
    }
}
