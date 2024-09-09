package com.qa.api.tests.POST;

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

public class CreateUserJsonStringCall {
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
        public void createUserTest() throws IOException {
            String JsonString="{\n" +
                    "    \"name\":\"Safaraaa\",\n" +
                    "    \"email\":\"safarqa2@gmail.com\",\n" +
                    "    \"gender\":\"male\",\n" +
                    "    \"status\":\"active\"\n" +
                    "}";
            APIResponse postapiResponse=apiContext.post("https://gorest.co.in/public/v2/users",
                    RequestOptions.create()
                            .setHeader("Content-Type","application/json")
                            .setHeader("Authorization","Bearer efb029b96faf13eb6914030455cac4cc91deb30611915c676e52c2446c2f8e77")
                            .setData(JsonString));
            System.out.println(postapiResponse.text());
            System.out.println(postapiResponse.status());
            Assert.assertEquals(postapiResponse.status(),201);
            Assert.assertEquals(postapiResponse.statusText(),"Created");
            System.out.println(postapiResponse.text());

            ObjectMapper objectMapper=new ObjectMapper();
            JsonNode postjsonreposne=objectMapper.readTree(postapiResponse.body());
            //fetch id from response
            String userId=postjsonreposne.get("id").asText();
            System.out.println("userid is "+userId);


            //get call
            APIResponse getApiresponse=apiContext.get("https://gorest.co.in/public/v2/users/"+userId,RequestOptions.create().setHeader("Authorization","Bearer efb029b96faf13eb6914030455cac4cc91deb30611915c676e52c2446c2f8e77"));
            Assert.assertEquals(getApiresponse.status(),200);
            System.out.println("--------get api response-----");
            System.out.println(getApiresponse.text());
            Assert.assertTrue(getApiresponse.text().contains(userId));
            Assert.assertTrue(getApiresponse.text().contains("Safaraaa"));
        }

        private String getRandomEmail() {
            String email="TestAutomation"+System.currentTimeMillis()+"@gmail.com";
            return email;

        }

    }


