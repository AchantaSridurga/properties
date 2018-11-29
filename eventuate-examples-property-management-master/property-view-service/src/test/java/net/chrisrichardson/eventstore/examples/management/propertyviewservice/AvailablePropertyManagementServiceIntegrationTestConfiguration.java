package net.chrisrichardson.eventstore.examples.management.propertyviewservice;

import io.eventuate.javaclient.spring.jdbc.EmbeddedTestAggregateStoreConfiguration;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.PropertyQuerysideRedisConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({PropertyQuerysideRedisConfiguration.class,
        EmbeddedTestAggregateStoreConfiguration.class})
public class AvailablePropertyManagementServiceIntegrationTestConfiguration {
}
