package net.chrisrichardson.eventstore.examples.management.propertyviewservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.management.property.common.Address;
import net.chrisrichardson.eventstore.examples.management.property.common.DeliveryTime;
import net.chrisrichardson.eventstore.examples.management.property.common.FindAvailablePropertyRequest;
import net.chrisrichardson.eventstore.examples.management.property.common.PropertyInfo;
import net.chrisrichardson.eventstore.examples.management.property.testutil.PropertyMother;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.backend.redis.PropertyQuerySideRedisService;
import net.chrisrichardson.eventstore.examples.management.propertyviewservice.web.PropertyViewController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PropertyViewControllerTest {

    @Mock
    PropertyQuerySideRedisService propertyQuerySideRedisService;

    @InjectMocks
    private PropertyViewController controller;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void findPropertyByIdTest() throws Exception {
        PropertyInfo propertyInfo = PropertyMother.makeProperty();
        ObjectMapper om = new ObjectMapper();

        when(propertyQuerySideRedisService.findById("1")).thenReturn(propertyInfo);

        MvcResult result = mockMvc.perform(get("/property/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        PropertyInfo resultingPropertyInfo = om.readValue(result.getResponse().getContentAsString(), PropertyInfo.class);
        assertEquals(propertyInfo, resultingPropertyInfo);
    }

    @Test
    public void findAvailablePropertyTest() throws Exception {
        ObjectMapper om = new ObjectMapper();

        PropertyInfo propertyInfo = PropertyMother.makeProperty();
        ArrayList<PropertyInfo> responseList = new ArrayList<>();
        responseList.add(propertyInfo);

        when(propertyQuerySideRedisService.findAvailableProperty(any(Address.class), any(DeliveryTime.class))).thenReturn(responseList);

        FindAvailablePropertyRequest request = new FindAvailablePropertyRequest("111111", 1, 12, 30);

        MvcResult result = mockMvc.perform(get("/property/availableproperty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<PropertyInfo> propertyInfoList = om.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PropertyInfo>>() {});
        assertFalse(propertyInfoList.isEmpty());
        assertTrue(propertyInfoList.size()==1);

        assertEquals(propertyInfo, propertyInfoList.get(0));
    }


}
