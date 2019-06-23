package com.codingdays.spring.springdata.controller;

import com.codingdays.users.business.UserBusinessServiceImpl;
import com.codingdays.users.web.users.UserController;
import com.codingdays.users.entities.User;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author dominik on 14.01.19
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private String expectedBilbo = "{\"firstName\": Bilbo, \"lastName\": \"Baggins\", \"id\": 1}";
    private String expectedMultipleCustomers = "[{\"firstName\": Will, \"lastName\": \"Smith\", \"id\": 1}," +
            "{\"firstName\": Peter, \"lastName\": \"Petigrew\", \"id\": 2}," +
            "{\"firstName\": Sam, \"lastName\": \"Crow\", \"id\": 3}]";
    private String expectedNull = "[{\"firstName\": null, \"lastName\": null, \"id\": 0}]";


    @Autowired
    MockMvc mock;

    @MockBean
    private UserBusinessServiceImpl businessService;

    /**
     * Asserting if retrieveAllCustomers is called propriately by the CustomerController.
     * The method should return the expected JSON object as defined in the when clause.
     *
     * @throws Exception
     */
    @Test
    public void getCustomers_basic() throws Exception {

        String expected = "[{\"firstName\": \"Will\", \"lastName\": \"Smith\", \"id\": 1}]";

        when(businessService.retrieveAllCustomers()).thenReturn(
                Arrays.asList(new User("Will", "Smith", "1"))
        );

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{firstName: Will, lastName: Smith, id:1}]"))
                .andReturn();

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), JSONCompareMode.NON_EXTENSIBLE);
    }

    /**
     * Asserting if getCustomers also returns multiple JSON objects defined as a
     * JSONArray.
     *
     * @throws Exception
     */
    @Test
    public void getCustomers_multipleCustomers_basic() throws Exception {

        when(businessService.retrieveAllCustomers()).thenReturn(
                Arrays.asList(new User("Will", "Smith", "1"),
                        new User("Peter", "Petigrew", "2"),
                        new User("Sam", "Crow", "3"))
        );

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{firstName: Will, lastName: Smith, id: 1}," +
                        "{firstName: Peter, lastName: Petigrew, id: 2}, " +
                        "{firstName: Sam, lastName: Crow, id: 3}]"))
                .andReturn();

        JSONAssert.assertEquals(expectedMultipleCustomers, result.getResponse().getContentAsString(), false);
    }

    /**
     * Asserting if an empty JSON object is also returned by the CustomerController.
     *
     * @throws Exception
     */
    @Test
    public void getCustomers_multipleCustomers_nullCustomer() throws Exception {
        when(businessService.retrieveAllCustomers()).thenReturn(
                Arrays.asList(new User())
        );

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"))
                .andReturn();

        JSONAssert.assertEquals(expectedNull, result.getResponse().getContentAsString(), false);
    }

    /**
     * Asserts if a users is retrieved by his or her id successfully.
     *
     * @throws Exception
     */
    @Test
    public void getCustomerById_basic() throws Exception {

        when(businessService.retrieveCustomerById("1")).thenReturn(
                Optional.of(new User("Bilbo", "Baggins", "1"))
        );

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/{Id}", 1)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{firstName: Bilbo, lastName: Baggins, id: 1}"))
                .andReturn();

        JSONAssert.assertEquals(expectedBilbo, result.getResponse().getContentAsString(), false);
    }


    /**
     * Asserting if the controller's post method accepts the data content of the HTTP post request.
     * The method should return a HTTP Status 200 (OK).
     *
     * @throws Exception
     */
    @Test
    public void saveCustomerTest_basic() throws Exception {

        User user = new User("Will", "Smith", "1");
        JSONObject jsonObject = new JSONObject("{\"firstName\": Will, \"lastName\": \"Smith\", \"id\": 1}");

        when(businessService.saveCustomer(Mockito.any(User.class))).thenReturn(
                user.toString()
        );

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users/new")
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString());

        MvcResult result = mock.perform(request)
                .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }


}