package net.chrisrichardson.eventstore.examples.management.propertyservice.backend;

import io.eventuate.AggregateRepository;
import io.eventuate.EventuateAggregateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class PropertyBackendConfiguration {

    @Bean
    public AggregateRepository<PropertyAggregate, PropertyCommand> propertyAggregateRepository(EventuateAggregateStore eventStore) {
        return new AggregateRepository<>(PropertyAggregate.class, eventStore);
    }

    @Bean
    public PropertyService propertyService(AggregateRepository<PropertyAggregate, PropertyCommand> propertyAggregateRepository) {
        return new PropertyService(propertyAggregateRepository);
    }
}
