package net.chrisrichardson.eventstore.examples.management.propertyservice;

import net.chrisrichardson.eventstore.examples.management.property.testutil.AbstractEntityEventTest;
import net.chrisrichardson.eventstore.examples.management.propertyservice.backend.PropertyAggregate;

public class PropertyEventTest  extends AbstractEntityEventTest {

    @Override
    protected Class<PropertyAggregate> entityClass() {
        return PropertyAggregate.class;
    }
}