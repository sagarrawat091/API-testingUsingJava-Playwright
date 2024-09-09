package com.qa.api.tests.POST;

import com.api.Data.UserLoombok;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class CreateUserUsingloombokPOJO {

    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext apiContext;

    @BeforeTest
    public void Setup()
    {
        playwright= Playwright.create();
        apiRequest=playwright.request();
        apiContext= apiRequest.newContext();
    }
    @AfterTest
    public void TearDown()
    {
        playwright.close();
    }

    @Test
    public void createUser() throws IOException {
        UserLoombok user=UserLoombok.builder()
                .name("Sagarrawat")
                .email("Safadas@gmail.com")
                .gender("male")
                .status("active").build();

        APIResponse postapiResponse = apiContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer efb029b96faf13eb6914030455cac4cc91deb30611915c676e52c2446c2f8e77")
                        .setData(user));
        System.out.println(postapiResponse.text());
        System.out.println(postapiResponse.status());
        Assert.assertEquals(postapiResponse.status(), 201);
        Assert.assertEquals(postapiResponse.statusText(), "Created");
        System.out.println(postapiResponse.text());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postjsonreposne = objectMapper.readTree(postapiResponse.body());
        //fetch id from response
        String userId = postjsonreposne.get("id").asText();
        System.out.println("userid is " + userId);
        String responseText = postapiResponse.text();

        //        change response text into json-deserilaization
        ObjectMapper objectMapper1 = new ObjectMapper();
        //        new user for reponse
        UserLoombok responsetextde = objectMapper1.readValue(responseText, UserLoombok.class);
        System.out.println("actual user created from response ");
        System.out.println(responsetextde);

        Assert.assertEquals(responsetextde.getName(), user.getName());
        Assert.assertEquals(responsetextde.getEmail(), user.getEmail());
        Assert.assertEquals(responsetextde.getGender(), user.getGender());
        Assert.assertEquals(responsetextde.getStatus(), user.getStatus());
    }
}
