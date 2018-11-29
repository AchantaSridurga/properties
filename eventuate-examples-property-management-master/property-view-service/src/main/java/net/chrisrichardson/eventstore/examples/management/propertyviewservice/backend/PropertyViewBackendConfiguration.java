package net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend;

import io.eventuate.javaclient.spring.EnableEventHandlers;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.redis.PropertyQuerySideRedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PropertyQuerysideRedisConfiguration.class})
@ComponentScan
@EnableEventHandlers
public class PropertyViewBackendConfiguration {


    @Bean
    public PropertyViewEventSubscriber propertyQueryWorkflow(PropertyQuerySideRedisService propertyQuerySideRedisService) {
        return new PropertyViewEventSubscriber(propertyQuerySideRedisService);
    }
}
