package net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend;

import io.eventuate.DispatchedEvent;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.property.common.event.PropertyCreatedEvent;
import net.chrisrichardson.eventstore.examples.management.property.common.event.PropertyDeletedEvent;
import net.chrisrichardson.eventstore.examples.management.property.common.event.PropertyUpdatedEvent;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.redis.PropertyQuerySideRedisService;

@EventSubscriber(id="propertyQuerySideEventHandlers")
public class PropertyViewEventSubscriber {

    private PropertyQuerySideRedisService propertyQuerySideRedisService;

    public PropertyViewEventSubscriber(PropertyQuerySideRedisService propertyQuerySideRedisService) {
        this.propertyQuerySideRedisService = propertyQuerySideRedisService;
    }

    @EventHandlerMethod
    public void create(DispatchedEvent<PropertyCreatedEvent> de) {
        PropertyCreatedEvent event = de.getEvent();
        String id = de.getEntityId();

        propertyQuerySideRedisService.add(id, event.getPropertyInfo());
    }

    @EventHandlerMethod
    public void update(DispatchedEvent<PropertyUpdatedEvent> de) {
        PropertyUpdatedEvent event = de.getEvent();
        String id = de.getEntityId();
        PropertyInfo propertyInfo = propertyQuerySideRedisService.findById(id);
        propertyQuerySideRedisService.delete(id, propertyInfo);
        propertyQuerySideRedisService.add(id, event.getPropertyInfo());
    }

    @EventHandlerMethod
    public void delete(DispatchedEvent<PropertyDeletedEvent> de) {
        String id = de.getEntityId();
        PropertyInfo propertyInfo = propertyQuerySideRedisService.findById(id);
        propertyQuerySideRedisService.delete(id, propertyInfo);
    }
}
