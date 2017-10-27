package am.ik.servicebroker.cloudamqp.client;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public enum CloudAmqpPlan {
    LEMUR, TIGER, BUNNY, RABBIT, PANDA, APE, HIPPO, LION;


    @JsonCreator
    public static CloudAmqpPlan of(String value) {
        return CloudAmqpPlan.valueOf(Objects.requireNonNull(value).toUpperCase());
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
