package net.chrisrichardson.eventstore.examples.management.propertyservice;

import net.chrisrichardson.eventstore.examples.management.property.common.CreatePropertyResponse;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.property.common.UpdatePropertyResponse;
import net.chrisrichardson.eventstore.examples.management.property.testutil.PropertyMother;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PropertyServiceIntegrationTestConfiguration.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0"})
public class PropertyServiceIntegrationTest {

  @Value("${local.server.port}")
  private int port;

  private String baseUrl(String path) {
    return "http://localhost:" + port + "/" + path;
  }

  @Autowired
  RestTemplate restTemplate;


  @Test
  public void shouldCreateAndUpdateProperty() {
    PropertyInfo propertyInfo = PropertyMother.makeProperty();

    ResponseEntity<CreatePropertyResponse> createPropertyResponse = restTemplate.postForEntity(baseUrl("/property"), propertyInfo, CreatePropertyResponse.class);
    Assert.assertEquals(createPropertyResponse.getStatusCode(), HttpStatus.OK);
    final String propertyId = createPropertyResponse.getBody().getPropertyId();

    ResponseEntity<UpdatePropertyResponse> updatePropertyResponse = restTemplate.exchange(baseUrl("/property/"+propertyId), HttpMethod.PUT, new HttpEntity(propertyInfo), UpdatePropertyResponse.class);
    Assert.assertEquals(updatePropertyResponse.getStatusCode(), HttpStatus.OK);

  }

}
