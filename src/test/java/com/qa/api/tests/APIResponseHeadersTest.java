package com.qa.api.tests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class APIResponseHeadersTest {
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
    @Test
    public void apiHeaderTest()
    {
        APIResponse apiResponse= apiContext.get("https://gorest.co.in/public/v2/users");
        Map<String,String> map= apiResponse.headers();
        map.forEach((k,v)-> System.out.println(k+": "+v));
        System.out.println("using header array+++++++");
        List<HttpHeader> list=apiResponse.headersArray();
        list.forEach((elem)-> System.out.println(elem.name+":"+elem.value));



    }






    @AfterTest
    public void TearDown()
    {
        playwright.close();
    }
}
