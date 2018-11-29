package net.chrisrichardson.eventstore.examples.management.propertyviewservice.web;

import net.chrisrichardson.eventstore.examples.management.property.commonweb.WebConfiguration;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.PropertyViewBackendConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import({PropertyViewBackendConfiguration.class, WebConfiguration.class})
public class PropertyViewWebConfiguration {
}
