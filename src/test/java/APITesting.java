import io.restassured.http.ContentType;

import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class APITesting {


    @Test
    public void registerUser()
    {

        given().auth().none()
                .contentType(ContentType.JSON)
                .body(new File("./Payload.json")) //Reading from Json file
                .post("https://reqres.in/api/register")
                .then().statusCode(200)
                .body("id", equalTo(4)
                , "token", equalTo("QpwL5tke4Pnpja7X4"));

        Response postResponse = given().auth().none()
                .contentType(ContentType.JSON)
                .body(new File("./Payload.json")).log().all()
                .post("https://reqres.in/api/register");
        postResponse.prettyPrint();
        System.out.println("The Response code for Register Success: "+postResponse.statusCode());
    }

    @Test
    public void loginUser()
    {
        String payload = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}";

        given().auth().none()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("https://reqres.in/api/login")
                .then().statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));

        Response postResponse = given().auth().none()
                .contentType(ContentType.JSON)
                .body(payload).log().all()
                .post("https://reqres.in/api/login");
        postResponse.prettyPrint();
        System.out.println("The Response code for Login Success: "+postResponse.statusCode());

    }

    @Test
    public void listResources()
    {
        given().auth().none()
                .contentType(ContentType.JSON)
                .when().get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body("page",equalTo(1), "per_page",equalTo(6),
                        "total",equalTo(12),
                        "total_pages",equalTo(2));
        Response getResponse = given().auth().none()
                .contentType(ContentType.JSON)
                .when().get("https://reqres.in/api/unknown");
        getResponse.prettyPrint();

        int id = getResponse.getBody().path("data[2].id");
        String name = getResponse.getBody().path("data[2].name");
        int year = getResponse.getBody().path("data[2].year");
        String color = getResponse.getBody().path("data[2].color");
        String pantone_value = getResponse.getBody().path("data[2].pantone_value");

        //Assertions implemented to check for data with id 3
        Assert.assertEquals(id,3);
        Assert.assertEquals(name,"true red");
        Assert.assertEquals(year,2002);
        Assert.assertEquals(color,"#BF1932");
        Assert.assertEquals(pantone_value,"19-1664");

        //printed response code
        System.out.println("The Response code for Login Success: "+getResponse.statusCode());


    }

}
