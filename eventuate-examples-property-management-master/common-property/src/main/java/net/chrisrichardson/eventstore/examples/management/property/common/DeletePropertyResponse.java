package net.chrisrichardson.eventstore.examples.management.property.common;

public class DeletePropertyResponse {

    private String version;

    public DeletePropertyResponse() {
    }

    public DeletePropertyResponse(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
