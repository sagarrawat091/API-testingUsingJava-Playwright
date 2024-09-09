package com.qa.api.tests.PUT;

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

//1 post call user id
//2 put call /userid
//3 get call
public class UpdateUserPUTCallWithPOJOLombok {
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
                .name("Sagar rawat 99")
                .email(getRandomEmail())
                .gender("male")
                .status("active").build();

        //1 Post Request
        APIResponse postapiResponse = apiContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer efb029b96faf13eb6914030455cac4cc91deb30611915c676e52c2446c2f8e77")
                        .setData(user));
        System.out.println(postapiResponse.text());
        System.out.println(postapiResponse.status());
        Assert.assertEquals(postapiResponse.status(), 201);
        Assert.assertEquals(postapiResponse.statusText(), "Created");

        //getting user id
        ObjectMapper mapper=new ObjectMapper();
        JsonNode node=mapper.readTree(postapiResponse.body());
        String user_id=node.get("id").asText();
        System.out.println("New User id "+user_id);

        //Put request
        //update the content
        user.setStatus("inactive");

        APIResponse PutResponse= apiContext.put("https://gorest.co.in/public/v2/users/"+user_id,RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer efb029b96faf13eb6914030455cac4cc91deb30611915c676e52c2446c2f8e77")
                .setData(user));

        System.out.println("put request status code "+PutResponse.status());
        Assert.assertEquals(PutResponse.status(),200);
        Assert.assertEquals(PutResponse.statusText(),"OK");
        //creating new user from pur response
        UserLoombok putuser=mapper.readValue(PutResponse.text(),UserLoombok.class);

        //assertion
        Assert.assertEquals(putuser.getName(),user.getName());
        Assert.assertEquals(putuser.getId(),user_id);
        Assert.assertEquals(putuser.getEmail(),user.getEmail());
        Assert.assertEquals(putuser.getStatus(),user.getStatus());
        Assert.assertEquals(putuser.getGender(),user.getGender());


        //get call
        APIResponse getResponse= apiContext.get("https://gorest.co.in/public/v2/users/"+user_id,RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer efb029b96faf13eb6914030455cac4cc91deb30611915c676e52c2446c2f8e77")
                .setData(user));

        System.out.println("get request status code "+getResponse.status());
        Assert.assertEquals(getResponse.status(),200);
        Assert.assertEquals(getResponse.statusText(),"OK");
        ObjectMapper mapper1=new ObjectMapper();
        JsonNode node2= mapper1.readTree(getResponse.body());
        System.out.println("the updated status is "+node2.get("status").asText());
        Assert.assertEquals(node2.get("status").asText(),"inactive");
}
    private String getRandomEmail() {
        String email="TestAutomation"+System.currentTimeMillis()+"@gmail.com";
        return email;

    }
}
