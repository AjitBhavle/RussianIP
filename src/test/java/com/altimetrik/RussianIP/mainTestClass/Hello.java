package com.altimetrik.RussianIP.mainTestClass;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.Test;

import com.altimetrik.RussianIP.baseClass.BaseTest;

public class Hello extends BaseTest{


	@Test(priority=0)
	public void postHelloSaysXML() {

		given().header("SOAPAction", "#SayHello").when().post("/mocked-soap-service.wsdl")
		.then().body("Hello.value", equalTo("My name is Ajit")).statusCode(200).contentType("application/xml");
		//assertThat().body("name", equalTo("Sarthak")).statusCode(200).extract().path("id");
		//System.out.println("JSON response: "+EmployeeId);

	}

	
	@Test(priority=1)
	public void getBasicAuthenticaiton() {
		System.out.println("This is test message");
		try {
			given().auth().basic("user", "pass").header("Accept", "application/json").parameter("search_term", "WireMock")
			.when().get("/basic-auth?")
			.then().statusCode(200);
		}catch(Exception e) {
			System.out.println(e.getCause() + e.getMessage());
		}
		
		//assertThat().body("name", equalTo("Sarthak")).statusCode(200).extract().path("id");
		//System.out.println("JSON response: "+EmployeeId);

	}


}
