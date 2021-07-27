package stepDefinitions;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

public class StepDefinition {
    public static Response response;
    public static Response responseText;
    public static RequestSpecification requestSpecification;
    public static JSONObject msgBody = new JSONObject();   
    public static Map<String, String> customHeaders = new HashMap();

    @Given("I use {string} as base URI")
    public void iUseAsBaseUri(String baseURI) {
        RestAssured.baseURI = baseURI;
    }

    @When("I make {string} call to {string}")
    public void i_make_call_to(String method, String totalPath) throws InterruptedException {
        Map<String, String> queryParams = new HashMap<String, String>();

        //Get queryParams
        if (totalPath.contains("?")) {
            String[] paramPairs = ((totalPath.split("\\?"))[1]).split("&");
            for (String pair : paramPairs) {
                String[] myPair = pair.split("=");
                if (myPair.length == 1) { //When param value is blank
                    queryParams.put(myPair[0], "");
                } else {
                    queryParams.put(myPair[0], myPair[1]);
                }
            }
        }

        //Get Path
        String path = (totalPath.split("\\?"))[0];

        RestAssured.basePath = path;
        requestSpecification = RestAssured.given().headers(customHeaders).queryParams(queryParams);
        switch (method.toUpperCase().trim()) {
            case "GET":
                response = requestSpecification.given().when().get();
                break;
            default:
                Assert.fail("Ooops!! Method [" + method + "] is not implemented.");
                break;
        }
    }

    @When("I make the request empty")
    public void i_make_the_request_empty() {
    	msgBody.clear();
    }

    @Then("I get response code {string}")
    public void i_get_response_code(String responseCode) {
        Assert.assertEquals(
            "Incorrect response code. Expected: " + responseCode + " Actual: " + response
                .statusCode(), Integer.parseInt(responseCode), response.statusCode());

    }

    @And("I verify response should match with contract file {string}")
    public void iVerifyResponseShouldMatchWithContractFile(String fileName) {
        String dirPath = "src/test/resources/testdata/responseContracts/" + fileName + ".json";
        response.then().assertThat().body(matchesJsonSchema(new File(dirPath)));
    }


    @Given("I reset API parameters") 
    public void iResetAPIParameters() {
        response = null;
        responseText = null;
        requestSpecification = null;
        msgBody.clear();
        customHeaders.clear();
    }

    @And("I verify the response is in {string} format")
    public void iVerifyTheResponseIsInFormat(String responseType) {
        Assert.assertTrue("Response type is not " + responseType,
            response.getHeader("content-type").contains(responseType));
    }

    @And("I verify the response contains only {string}")
    public void iVerifyTheResponseContainsOnly(String fieldList) throws ParseException {
        List<String> expectedFields;
        List<String> actualFields;

        JSONObject responseJson =
            (JSONObject) new JSONParser().parse(response.getBody().asString());

        expectedFields = Arrays.asList((fieldList.trim()).replace(" ", "").split(","));
        for (int idx = 0; idx < expectedFields.size(); idx++) {
            expectedFields.set(idx, expectedFields.get(idx).replaceAll("\\..*$", ""));
        }
        actualFields = new ArrayList<String>(responseJson.keySet());
        Collections.sort(expectedFields, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(actualFields, String.CASE_INSENSITIVE_ORDER);
        Assert.assertEquals("Selective field mismatch.", expectedFields, actualFields);

    }
}
