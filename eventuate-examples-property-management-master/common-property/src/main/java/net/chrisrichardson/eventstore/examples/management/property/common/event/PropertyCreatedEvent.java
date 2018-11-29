package net.chrisrichardson.eventstore.examples.management.property.common.event;

import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;

public class PropertyCreatedEvent extends PropertyEvent {
    private PropertyInfo propertyInfo;

    public PropertyCreatedEvent() {
    }

    public PropertyCreatedEvent(PropertyInfo propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    public PropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    public void setPropertyInfo(PropertyInfo propertyInfo) {
        this.propertyInfo = propertyInfo;
    }
}
