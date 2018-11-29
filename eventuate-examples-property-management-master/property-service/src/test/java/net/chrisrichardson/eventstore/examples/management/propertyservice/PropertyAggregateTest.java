package net.chrisrichardson.eventstore.examples.management.propertyservice;

import io.eventuate.Event;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.property.common.event.PropertyCreatedEvent;
import net.chrisrichardson.eventstore.examples.management.property.common.event.PropertyUpdatedEvent;
import net.chrisrichardson.eventstore.examples.management.property.testutil.PropertyMother;
import net.chrisrichardson.eventstore.examples.management.propertyservice.backend.CreatePropertyCommand;
import net.chrisrichardson.eventstore.examples.management.propertyservice.backend.PropertyAggregate;
import net.chrisrichardson.eventstore.examples.management.propertyservice.backend.UpdatePropertyCommand;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PropertyAggregateTest {

  @Test
  public void testPropertyCommands() {
    PropertyAggregate propertyAggregate = new PropertyAggregate();
    PropertyInfo propertyInfo = PropertyMother.makeProperty();

    List<Event> events = propertyAggregate.process(new CreatePropertyCommand(propertyInfo));

    Assert.assertEquals(1, events.size());
    Assert.assertEquals(PropertyCreatedEvent.class, events.get(0).getClass());

    propertyAggregate.applyEvent(events.get(0));
    Assert.assertEquals(propertyInfo, propertyAggregate.getProperty());

    propertyInfo.setType("cantina");
    events = propertyAggregate.process(new UpdatePropertyCommand(propertyInfo));

    Assert.assertEquals(1, events.size());
    Assert.assertEquals(PropertyUpdatedEvent.class, events.get(0).getClass());

    propertyAggregate.applyEvent(events.get(0));
    Assert.assertEquals(propertyInfo, propertyAggregate.getProperty());
  }
}
