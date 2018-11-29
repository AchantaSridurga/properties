package net.chrisrichardson.eventstore.examples.management.property.common;


public class CreatePropertyResponse {

  private String propertyId;

  public CreatePropertyResponse() {
  }

  public CreatePropertyResponse(String propertyId) {
    this.propertyId = propertyId;
  }

  public String getPropertyId() {
    return propertyId;
  }

  public void setPropertyId(String propertyId) {
    this.propertyId = propertyId;
  }
}
