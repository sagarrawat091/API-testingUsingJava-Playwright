package com.qa.api.tests;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DisposeClassTest {
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
    public void checkDisposeMethod()
    {
        APIResponse apiResponse= apiContext.get("https://gorest.co.in/public/v2/users");
//       / System.out.println(apiResponse.text());
        APIResponse apiResponse1= apiContext.get("https://reqres.in/api/users?page=2");
//        apiResponse.dispose();
        apiContext.dispose();
        try {
            System.out.println(apiResponse.text());
        }catch (PlaywrightException e){
            System.out.println("api response  is disposed");
        }try {
            System.out.println(apiResponse1.text());
        }catch (PlaywrightException e){
            System.out.println("api response 1 is disposed");
        }
//        System.out.println(apiResponse1.text());


    }



    @AfterTest
    public void TearDown()
    {
        playwright.close();
    }
}
