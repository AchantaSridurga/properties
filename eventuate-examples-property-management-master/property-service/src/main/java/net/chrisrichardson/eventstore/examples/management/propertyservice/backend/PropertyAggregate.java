package net.chrisrichardson.eventstore.examples.management.propertyservice.backend;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.property.common.event.PropertyCreatedEvent;
import net.chrisrichardson.eventstore.examples.management.property.common.event.PropertyDeletedEvent;
import net.chrisrichardson.eventstore.examples.management.property.common.event.PropertyUpdatedEvent;

import java.util.ArrayList;
import java.util.List;

public class PropertyAggregate  extends ReflectiveMutableCommandProcessingAggregate<PropertyAggregate, PropertyCommand> {
    private PropertyInfo property;
    private boolean deleted = false;

    public List<Event> process(CreatePropertyCommand cmd) {
        return EventUtil.events(new PropertyCreatedEvent(cmd.getPropertyInfo()));
    }

    public List<Event> process(UpdatePropertyCommand cmd) {
        if(!this.deleted) {
            return EventUtil.events(new PropertyUpdatedEvent(cmd.getPropertyInfo()));
        }
        return new ArrayList<>();
    }

    public List<Event> process(DeletePropertyCommand cmd) {
        if(!this.deleted) {
            return EventUtil.events(new PropertyDeletedEvent());
        }
        return new ArrayList<>();
    }

    public void apply(PropertyCreatedEvent event) {
        this.property = event.getPropertyInfo();
    }

    public void apply(PropertyUpdatedEvent event) {
        this.property = event.getPropertyInfo();
    }

    public void apply(PropertyDeletedEvent event) {
        this.deleted = true;
    }

    public PropertyInfo getProperty() {
        return property;
    }
}
