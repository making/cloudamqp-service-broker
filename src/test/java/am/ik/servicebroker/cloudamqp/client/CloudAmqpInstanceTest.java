package am.ik.servicebroker.cloudamqp.client;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CloudAmqpInstanceTest {
    @Test
    public void dashboardUrl() throws Exception {
        CloudAmqpInstance instance = new CloudAmqpInstance("a",
                CloudAmqpPlan.LEMUR,
                CloudAmqpRegion.AMAZON_WEB_SERVICES_AP_NORTHEAST_1,
                "hoge", "amqp://abc:def@foo.rmq.cloudamqp.com/bar");
        assertThat(instance.dashboardUrl()).isEqualTo("https://foo.rmq.cloudamqp.com/#/login/abc/def");
    }

    @Test
    public void credentials() throws Exception {
        CloudAmqpInstance instance = new CloudAmqpInstance("a",
                CloudAmqpPlan.LEMUR,
                CloudAmqpRegion.AMAZON_WEB_SERVICES_AP_NORTHEAST_1,
                "hoge", "amqp://abc:def@foo.rmq.cloudamqp.com/bar");
        Map<String, Object> credentials = instance.credentials();
        assertThat(credentials.get("hostname")).isEqualTo("foo.rmq.cloudamqp.com");
        assertThat(credentials.get("username")).isEqualTo("abc");
        assertThat(credentials.get("password")).isEqualTo("def");
        assertThat(credentials.get("port")).isEqualTo(5672);
        assertThat(credentials.get("vhost")).isEqualTo("bar");
        assertThat(credentials.get("uri")).isEqualTo("amqp://abc:def@foo.rmq.cloudamqp.com/bar");
        assertThat(credentials.get("dashboard_url")).isEqualTo("https://foo.rmq.cloudamqp.com/#/login/abc/def");
    }
}