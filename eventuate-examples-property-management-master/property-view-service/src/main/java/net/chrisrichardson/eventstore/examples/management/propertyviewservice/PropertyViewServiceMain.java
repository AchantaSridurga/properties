package net.chrisrichardson.eventstore.examples.management.propertyviewservice;

import io.eventuate.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.management.property.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.web.PropertyViewWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PropertyViewWebConfiguration.class,
        EventuateDriverConfiguration.class,
        CommonSwaggerConfiguration.class})
@EnableAutoConfiguration
@ComponentScan
public class PropertyViewServiceMain {

  public static void main(String[] args) {
    SpringApplication.run(PropertyViewServiceMain.class, args);
  }
}
