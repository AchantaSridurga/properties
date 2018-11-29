package net.chrisrichardson.eventstore.examples.management.propertyservice.web;

import net.chrisrichardson.eventstore.examples.management.property.commonweb.WebConfiguration;
import net.chrisrichardson.eventstore.examples.management.propertyservice.backend.PropertyBackendConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import({PropertyBackendConfiguration.class, WebConfiguration.class})
public class PropertyWebConfiguration {
}
