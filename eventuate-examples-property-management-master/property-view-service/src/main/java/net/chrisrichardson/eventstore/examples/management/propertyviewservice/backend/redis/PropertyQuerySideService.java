package net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.redis;

import net.chrisrichardson.eventstore.examples.management.property.common.Address;
import net.chrisrichardson.eventstore.examples.management.property.common.DeliveryTime;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;

import java.util.List;

public interface PropertyQuerySideService {

    void add(final String id, final PropertyInfo property);

    void delete(String id, PropertyInfo property);

    List<PropertyInfo> findAvailableProperty(Address deliveryAddress, DeliveryTime deliveryTime);

    PropertyInfo findById(String id);
}
