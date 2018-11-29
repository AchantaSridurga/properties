package net.chrisrichardson.eventstore.examples.management.propertyservice.backend;

import io.eventuate.AggregateRepository;
import io.eventuate.EntityWithIdAndVersion;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;

import java.util.concurrent.CompletableFuture;

public class PropertyService {

    private final AggregateRepository<PropertyAggregate, PropertyCommand> repository;

    public PropertyService(AggregateRepository<PropertyAggregate, PropertyCommand> repository) {
        this.repository = repository;
    }

    public CompletableFuture<EntityWithIdAndVersion<PropertyAggregate>> createProperty(PropertyInfo propertyInfo) {
        return repository.save(new CreatePropertyCommand(propertyInfo));
    }

    public CompletableFuture<EntityWithIdAndVersion<PropertyAggregate>> updateProperty(String id, PropertyInfo propertyInfo) {
        return repository.update(id, new UpdatePropertyCommand(propertyInfo));
    }

    public CompletableFuture<EntityWithIdAndVersion<PropertyAggregate>> deleteProperty(String id) {
        return repository.update(id, new DeletePropertyCommand());
    }
}
