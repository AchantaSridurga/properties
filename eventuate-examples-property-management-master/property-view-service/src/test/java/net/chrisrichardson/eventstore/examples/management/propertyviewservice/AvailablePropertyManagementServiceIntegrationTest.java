package net.chrisrichardson.eventstore.examples.management.propertyviewservice;

import net.chrisrichardson.eventstore.examples.management.property.common.Address;
import net.chrisrichardson.eventstore.examples.management.property.common.DeliveryTime;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.property.testutil.PropertyMother;
import net.chrisrichardson.eventstore.examples.management.property.testutil.PropertyTestData;
import net.chrisrichardson.eventstore.examples.management.property.testutil.TestUtil;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.redis.PropertyQuerySideService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AvailablePropertyManagementServiceIntegrationTestConfiguration.class)
@IntegrationTest
public class AvailablePropertyManagementServiceIntegrationTest {

    @Autowired
    PropertyQuerySideService propertyQuerySideService;

    private void assertFoundAjanta(List<PropertyInfo> results, long timestamp) {
        assertFoundProperty("Ajanta"+timestamp, results);
    }

    private void assertFoundProperty(String expectedName, List<PropertyInfo> results) {
        Assert.assertTrue(results.stream().filter(propertyInfo -> propertyInfo.getName().equals(expectedName)).findFirst().isPresent());
    }

    int counter = 0;

    private String add(PropertyInfo propertyInfo) {
        String id = "r" + System.currentTimeMillis() + "-" + counter++;
        propertyQuerySideService.add(id, propertyInfo);
        return id;
    }

    private void update(String id, PropertyInfo propertyInfo) {
        PropertyInfo ri = propertyQuerySideService.findById(id);
        propertyQuerySideService.delete(id, ri);
        propertyQuerySideService.add(id, propertyInfo);
    }

    private void delete(String id) {
        PropertyInfo ri = propertyQuerySideService.findById(id);
        propertyQuerySideService.delete(id, ri);
    }

    @Test
    public void testSomething() {
        long timestamp = System.currentTimeMillis();

        String ajantaId = add(PropertyMother.makeProperty(timestamp));
        add(PropertyMother.makeEggShopProperty(timestamp));
        add(PropertyMother.makeLateNightTacos(timestamp));

        TestUtil.awaitPredicateTrue(t -> propertyQuerySideService.findById(ajantaId),
                propertyInfo -> propertyInfo != null);


        DeliveryTime deliveryTime = PropertyTestData.makeGoodDeliveryTime();
        Address deliveryAddress = PropertyTestData.getADDRESS1();
        List<PropertyInfo> results = propertyQuerySideService.findAvailableProperty(deliveryAddress, deliveryTime);
        assertFoundAjanta(results, timestamp);
    }

    @Test
    public void testFindAvailableProperty_Monday8Am() {
        long timestamp = System.currentTimeMillis();

        add(PropertyMother.makeProperty(timestamp));
        String eggshopId = add(PropertyMother.makeEggShopProperty(timestamp));
        add(PropertyMother.makeLateNightTacos(timestamp));

        TestUtil.awaitPredicateTrue(t -> propertyQuerySideService.findById(eggshopId),
                propertyInfo -> propertyInfo != null);

        DeliveryTime deliveryTime = PropertyTestData.makeDeliveryTime(Calendar.MONDAY, 8, 0);
        Address deliveryAddress = PropertyTestData.getADDRESS1();
        List<PropertyInfo> results = propertyQuerySideService.findAvailableProperty(deliveryAddress, deliveryTime);
        assertFoundProperty(PropertyMother.MONTCLAIR_EGGSHOP+timestamp, results);
    }

    @Test
    public void testFindAvailableProperty_None() {
        long timestamp = System.currentTimeMillis();

        String ajantaId = add(PropertyMother.makeProperty(timestamp));
        String eggshopId = add(PropertyMother.makeEggShopProperty(timestamp));
        String lateNightSnackId = add(PropertyMother.makeLateNightTacos(timestamp));

        TestUtil.awaitPredicateTrue(t -> propertyQuerySideService.findById(ajantaId),
                propertyInfo -> propertyInfo != null);
        TestUtil.awaitPredicateTrue(t -> propertyQuerySideService.findById(eggshopId),
                propertyInfo -> propertyInfo != null);
        TestUtil.awaitPredicateTrue(t -> propertyQuerySideService.findById(lateNightSnackId),
                propertyInfo -> propertyInfo != null);

        DeliveryTime deliveryTime = PropertyTestData.makeBadDeliveryTime();
        Address deliveryAddress = PropertyTestData.getADDRESS1();
        List<PropertyInfo> results = propertyQuerySideService.findAvailableProperty(deliveryAddress, deliveryTime);
        Assert.assertTrue(results.isEmpty());
    }

    private void updatePropertyOpeningHours(PropertyInfo property) {
        property.setOpeningHours(PropertyMother.makeOpeningHours(PropertyMother.OPENING_MINUTE - 1));
    }

    @Test
    public void testUpdateProperty() {
        long timestamp = System.currentTimeMillis();
        
        PropertyInfo ajantaProperty = PropertyMother.makeProperty(timestamp);
        String ajantaId = add(ajantaProperty);
        add(PropertyMother.makeEggShopProperty(timestamp));
        add(PropertyMother.makeLateNightTacos(timestamp));

        TestUtil.awaitPredicateTrue(t -> propertyQuerySideService.findById(ajantaId),
                propertyInfo -> propertyInfo != null);

        updatePropertyOpeningHours(ajantaProperty);
        update(ajantaId, ajantaProperty);

        TestUtil.awaitPredicateTrue(t -> propertyQuerySideService.findById(ajantaId),
                propertyInfo -> propertyInfo.getOpeningHours().equals(ajantaProperty.getOpeningHours()));

        DeliveryTime deliveryTime = PropertyTestData.getTimeTomorrow(PropertyMother.OPENING_HOUR, PropertyMother.OPENING_MINUTE - 1);
        Address deliveryAddress = PropertyTestData.getADDRESS1();
        List<PropertyInfo> results = propertyQuerySideService.findAvailableProperty(deliveryAddress, deliveryTime);
        assertFoundAjanta(results, timestamp);

    }
}
