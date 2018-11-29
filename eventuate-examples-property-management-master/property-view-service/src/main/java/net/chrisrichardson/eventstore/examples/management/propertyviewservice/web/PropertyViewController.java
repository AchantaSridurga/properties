package net.chrisrichardson.eventstore.examples.management.propertyviewservice.web;

import net.chrisrichardson.eventstore.examples.management.property.common.Address;
import net.chrisrichardson.eventstore.examples.management.property.common.DeliveryTime;
import net.chrisrichardson.eventstore.examples.management.property.common.FindAvailablepropertysRequest;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.redis.PropertyQuerySideRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/property")
public class PropertyViewController {

  @Autowired
  private PropertyQuerySideRedisService propertyQuerySideRedisService;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<PropertyInfo> findProperty(@PathVariable("id") String propertyId) {
    PropertyInfo r = propertyQuerySideRedisService.findById(propertyId);
    return r == null ?  new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(r);
  }

  @RequestMapping(value = "/availableproperty", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<List<PropertyInfo>> findAvailableProperty(@ModelAttribute FindAvailablePropertyRequest request) {
    Address address = request.makeAddress();
    DeliveryTime deliveryTime = request.makeDeliveryTime();
    return ResponseEntity.ok(propertyQuerySideRedisService.findAvailableProperty(address, deliveryTime));
  }
}
