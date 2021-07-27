package Cucumber.options;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class) @CucumberOptions(features = "src/test/java/features", glue = {
    "stepDefinitions"}, tags = "@ipstack4", monochrome = true, plugin = {"pretty",
    "json:target/Cucumber.json", "html:target/cucumber-reports/Cucumber.html"})

public class TestRunner {
}
