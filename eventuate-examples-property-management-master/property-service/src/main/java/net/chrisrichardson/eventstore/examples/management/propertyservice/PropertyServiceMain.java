package net.chrisrichardson.eventstore.examples.management.propertyservice;

import io.eventuate.javaclient.driver.EventuateDriverConfiguration;
import net.chrisrichardson.eventstore.examples.management.property.commonswagger.CommonSwaggerConfiguration;
import net.chrisrichardson.eventstore.examples.management.propertyservice.web.PropertyWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PropertyWebConfiguration.class,
        EventuateDriverConfiguration.class,
        CommonSwaggerConfiguration.class})
@EnableAutoConfiguration
@ComponentScan
public class PropertyServiceMain {

  public static void main(String[] args) {
    SpringApplication.run(PropertyServiceMain.class, args);
  }
}
