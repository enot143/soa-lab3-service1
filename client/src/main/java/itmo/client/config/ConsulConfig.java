package itmo.client.config;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Collections;

@Startup
@Singleton
public class ConsulConfig {
    String serviceId = "soa-3-1.0-SNAPSHOT";
    Consul client;
    AgentClient agentClient;

    @PostConstruct
    void init() {
        client = Consul.builder().build();
        agentClient = client.agentClient();
        Registration service = ImmutableRegistration.builder()
                .id(serviceId)
                .name(serviceId)
                .port(4567)
                .address("localhost")
                .check(Registration.RegCheck.http("http://localhost:4567/soa-3-1.0-SNAPSHOT/routes", 10, 1))
                .tags(Collections.singletonList("tag1"))
                .meta(Collections.singletonMap("version", "1.0"))
                .build();

        agentClient.register(service);
    }

    @PreDestroy
    private void deregisterService(){
        agentClient.deregister(serviceId);
    }

}
