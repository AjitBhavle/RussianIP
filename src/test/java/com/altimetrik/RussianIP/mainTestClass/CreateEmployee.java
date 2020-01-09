package com.altimetrik.RussianIP.mainTestClass;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.altimetrik.RussianIP.baseClass.BaseTest;
import com.altimetrik.RussianIP.pojo.EmployeePojo;

public class CreateEmployee extends BaseTest {

	EmployeePojo CreateEmployeePojo=new EmployeePojo();
	int EmployeeId;
	
	
	
	@Test(priority=0)
	public void postEmployee() {
		try {
			CreateEmployeePojo.setEmployeeProp("name", "Sarthak");
			CreateEmployeePojo.setEmployeeProp("salary", "123");
			CreateEmployeePojo.setEmployeeProp("age", "23");
			EmployeeId = given().contentType("application/json")
					.body(CreateEmployeePojo)
					.when().post("/create")
					.then().extract().jsonPath().getInt("id");
					//extract().xmlPath().getInt("id");
			//assertThat().body("name", equalTo("Sarthak")).statusCode(200).extract().path("id");
			//System.out.println("Response: "+res);
			///EmployeeId = res.htmlPath().getInt("id");
			System.out.println("JSON response: "+EmployeeId);
		}catch(Exception e) {
			System.out.println("Cause is: "+e.getCause()+"Message is: "+e.getMessage());
		}
		
	}
	@Test(priority=1)
	public void postEmployeeWithDupRecord() {

		given().contentType("application/json")
		.body(CreateEmployeePojo)
		.when().post("/create")
		.then().body(containsString("Duplicate entry 'Sarthak' for key 'employee_name_unique'")).statusCode(200);
	}
	@Test(priority=2)
	public void getEmployeeDetailsByValidId() {

		given().pathParameter("id", EmployeeId).contentType("application/json")
		.when().get("/employee/{id}")//get("http://inventorymanagement.mocklab.io/battery?batteryId=1")
		.then().extract().htmlPath().getString("employee_name").equals("Sarthak");

	}
	
	@Test(priority=3)
	public void DeleteEmployee() {
		System.out.println("JSON response: "+EmployeeId);
		given().pathParameter("id", EmployeeId)
		.when().delete("/delete/{id}").then().body(containsString("successfully! deleted Records"))
		.statusCode(200);
	}

}
