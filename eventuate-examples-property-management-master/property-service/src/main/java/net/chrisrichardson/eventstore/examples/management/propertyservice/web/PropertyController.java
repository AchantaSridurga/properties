package net.chrisrichardson.eventstore.examples.management.propertyservice.web;

import net.chrisrichardson.eventstore.examples.management.property.common.CreatePropertyResponse;
import net.chrisrichardson.eventstore.examples.management.property.common.DeletePropertyResponse;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.property.common.UpdatePropertyResponse;
import net.chrisrichardson.eventstore.examples.management.propertyservice.backend.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/property")
public class PropertyController {

  private PropertyService propertyService;

  @Autowired
  public PropertyController(PropertyService propertyService) {
    this.propertyService = propertyService;
  }

  @RequestMapping(method = RequestMethod.POST)
  public CompletableFuture<CreatePropertyResponse> createProperty(@RequestBody @Valid PropertyInfo request) {
    return propertyService.createProperty(request)
            .thenApply(entityAndEventInfo -> new CreatePropertyResponse(entityAndEventInfo.getEntityId()));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public CompletableFuture<UpdatePropertyResponse> updateProperty(@PathVariable("id") String propertyId,
                                                                      @RequestBody  @Valid PropertyInfo request) {
    return propertyService.updateProperty(propertyId, request)
            .thenApply(entityAndEventInfo -> new UpdatePropertyResponse(entityAndEventInfo.getEntityVersion().asString()));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public CompletableFuture<DeletePropertyResponse> deleteProperty(@PathVariable("id") String propertyId) {
    return propertyService.deleteProperty(propertyId).thenApply(entityAndEventInfo -> new DeletePropertyResponse(entityAndEventInfo.getEntityVersion().asString()));
  }
}
