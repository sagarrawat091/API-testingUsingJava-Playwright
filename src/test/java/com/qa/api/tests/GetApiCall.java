package com.qa.api.tests;

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
import java.util.Map;

public class GetApiCall {
    Playwright playwright;
    APIRequest apiRequest;
    APIRequestContext apiContext;

    @BeforeTest
    public void Setup()
    {
        playwright=Playwright.create();
        apiRequest=playwright.request();
        apiContext= apiRequest.newContext();
    }

    //query parameter
    @Test
    public void getSpecificUser()
    {
        APIResponse apiResponse= apiContext.get("https://gorest.co.in/public/v2/users",
                RequestOptions.create().setQueryParam("gender","male"));
        System.out.println("Status code =>"+apiResponse.status());
        System.out.println(apiResponse.text());
        Assert.assertEquals(apiResponse.status(),200);

    }
    @Test
    public void  getUserApiTest() throws IOException {
        APIResponse apiResponse= apiContext.get("https://gorest.co.in/public/v2/users");
//        System.out.println(apiResponse.body());//byte array

        int status=apiResponse.status();
        System.out.println(status);

        System.out.println("--------Response body--------------");
        ObjectMapper objectMapper=new ObjectMapper();
        JsonNode jsonNode= objectMapper.readTree(apiResponse.body());
        System.out.println(jsonNode.toPrettyString());

        System.out.println("------------url---------------------");
        System.out.println(apiResponse.url());
        System.out.println("-----headers---------");
        Map<String,String> header=apiResponse.headers();
        System.out.println(header);
        Assert.assertEquals(header.get("content-type"),"application/json; charset=utf-8");
        Assert.assertEquals(header.get("x-download-options"),"noopen");

    }
    @AfterTest
    public void TearDown()
    {
        playwright.close();
    }
}
