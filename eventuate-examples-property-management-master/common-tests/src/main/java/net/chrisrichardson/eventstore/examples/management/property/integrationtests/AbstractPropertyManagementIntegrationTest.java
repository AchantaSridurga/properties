package net.chrisrichardson.eventstore.examples.management.property.integrationtests;

import net.chrisrichardson.eventstore.examples.management.property.common.*;
import net.chrisrichardson.eventstore.examples.management.property.testutil.PropertyMother;
import net.chrisrichardson.eventstore.examples.management.property.testutil.PropertyTestData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static net.chrisrichardson.eventstore.examples.management.property.testutil.TestUtil.awaitNotFound;
import static net.chrisrichardson.eventstore.examples.management.property.testutil.TestUtil.awaitPredicateTrue;
import static net.chrisrichardson.eventstore.examples.management.property.testutil.TestUtil.awaitSuccessfulRequest;
import static org.junit.Assert.assertEquals;

public abstract class AbstractPropertyManagementIntegrationTest {

    private String commandSideUrl(String path) {
        return "http://" + getHost() + ":" + getCommandSidePort() + "/" + path;
    }

    private String querySideUrl(String path) {
        return "http://" + getHost() + ":" + getQuerySidePort() + "/" + path;
    }

    @Autowired
    RestTemplate restTemplate;


    @Test
    public void shouldCreateUpdateAndDeleteProperty() {
        PropertyInfo propertyInfo = PropertyMother.makeProperty();

        String propertyId = createPropertyAndAwaitCreationInView(propertyInfo);
        PropertyInfo createdProperty = getProperty(propertyId);
        assertEquals(propertyInfo, createdProperty);

        PropertyInfo propertyInfoToUpdate =  propertyInfo;
        propertyInfoToUpdate.setName("New Bar");

        ResponseEntity<UpdatePropertyResponse> updatePropertyResponse = restTemplate.exchange(commandSideUrl("/property/"+propertyId), HttpMethod.PUT, new HttpEntity(propertyInfoToUpdate), UpdatePropertyResponse.class);
        assertEquals(updatePropertyResponse.getStatusCode(), HttpStatus.OK);

        awaitPredicateTrue(t -> restTemplate.getForEntity(querySideUrl("/property/"+propertyId), PropertyInfo.class),
                responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null && responseEntity.getBody().getName().equals("New Bar"));

        PropertyInfo updatedProperty = getProperty(propertyId);
        assertEquals(propertyInfoToUpdate, updatedProperty);

        restTemplate.exchange(commandSideUrl("/property/"+propertyId), HttpMethod.DELETE, HttpEntity.EMPTY, UpdatePropertyResponse.class);
        awaitPropertyDeletionInView(propertyId);
    }

    @Test
    public void shouldFindAvailableProperty() {
        long timestamp = System.currentTimeMillis();

        createPropertyAndAwaitCreationInView(PropertyMother.makeProperty(timestamp));
        createPropertyAndAwaitCreationInView(PropertyMother.makeEggShopProperty(timestamp));
        createPropertyAndAwaitCreationInView(PropertyMother.makeLateNightTacos(timestamp));

        DeliveryTime deliveryTime = PropertyTestData.makeGoodDeliveryTime();
        Address deliveryAddress = PropertyTestData.getADDRESS1();

        FindAvailablePropertysRequest request = new FindAvailablePropertysRequest(deliveryAddress.getZip(), deliveryTime.getDayOfWeek(), deliveryTime.getHour(), deliveryTime.getMinute());

        ResponseEntity<PropertyInfo[]> availableProperty = restTemplate.getForEntity(querySideUrl("/property/availableproperty?zipcode="+deliveryAddress.getZip()+"&dayOfWeek="+deliveryTime.getDayOfWeek()+"&hour="+deliveryTime.getHour()+"&minute="+deliveryTime.getMinute()), PropertyInfo[].class);

        assertFoundAjanta(Arrays.asList(availableProperty.getBody()), timestamp);
    }

    private PropertyInfo getProperty(String id) {
        ResponseEntity<PropertyInfo> responseEntity = restTemplate.getForEntity(querySideUrl("/property/"+id), PropertyInfo.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        return responseEntity.getBody();
    }

    protected PropertyInfo awaitPropertyCreationInView(String id) {
        return (PropertyInfo) awaitSuccessfulRequest(() -> restTemplate.getForEntity(querySideUrl("/property/"+id), PropertyInfo.class));
    }

    protected void awaitPropertyDeletionInView(String id) {
        awaitNotFound(() -> restTemplate.getForEntity(querySideUrl("/property/"+id), PropertyInfo.class));
    }

    private String createPropertyAndAwaitCreationInView(PropertyInfo propertyInfo) {
        ResponseEntity<CreatePropertyResponse> createPropertyResponse = restTemplate.postForEntity(commandSideUrl("/property"), propertyInfo, CreatePropertyResponse.class);
        assertEquals(createPropertyResponse.getStatusCode(), HttpStatus.OK);
        final String propertyId = createPropertyResponse.getBody().getPropertyId();

        awaitPropertyCreationInView(propertyId);

        return propertyId;
    }

    private void assertFoundAjanta(List<PropertyInfo> results, long timestamp) {
        assertFoundProperty("Ajanta"+timestamp, results);
    }

    private void assertFoundProperty(String expectedName, List<PropertyInfo> results) {
        Assert.assertTrue(results.stream().filter(propertyInfo -> propertyInfo.getName().equals(expectedName)).findFirst().isPresent());
    }

    protected abstract String getHost();

    protected abstract int getCommandSidePort();

    protected abstract int getQuerySidePort();
}
