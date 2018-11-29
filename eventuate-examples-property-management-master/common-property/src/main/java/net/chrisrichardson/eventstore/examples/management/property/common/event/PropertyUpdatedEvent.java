package net.chrisrichardson.eventstore.examples.management.property.common.event;

import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;

public class PropertyUpdatedEvent extends PropertyEvent {
    private PropertyInfo propertyInfo;

    public PropertyUpdatedEvent() {
    }

    public PropertyUpdatedEvent(PropertyInfo propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    public PropertyInfo getPropertyInfo() {
        return propertyInfo;
    }

    public void setPropertyInfo(PropertyInfo propertyInfo) {
        this.propertyInfo = propertyInfo;
    }
}