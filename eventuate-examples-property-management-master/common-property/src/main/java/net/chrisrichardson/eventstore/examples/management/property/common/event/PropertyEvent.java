package net.chrisrichardson.eventstore.examples.management.property.common.event;


import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity="net.chrisrichardson.eventstore.examples.management.propertysservice.backend.PropertyAggregate")
public abstract  class PropertyEvent implements Event {
}