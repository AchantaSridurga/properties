package net.chrisrichardson.eventstore.examples.management.property.common;


public class UpdatePropertyResponse {

  private String version;

  public UpdatePropertyResponse() {
  }

  public UpdatePropertyResponse(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}
