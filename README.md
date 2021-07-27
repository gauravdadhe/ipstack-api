# ipStack-API
ipStack API Automation project

## Technical stack used.
  1. End point documentation: https://ipstack.com/documentation
  2. Required scripting language: Java 
  3. JDK: JDK 11
  4. Maven: 3.6.1
  5. Required testing tool: Rest-assured, Cucumber-BDD framework with Junit
  6. CI platform: git, jenkins

## How to run the tests locally
The tests can be run locally using below command in terminal
```java
mvn test -Dcucumber.options="--tags @api"
```

The reports of the execution are available in 3 formats,
* cucumber --> Available at *target/Cucumber.json*
* html --> Available at *target/cucumber-reports/Cucumber.html*
* html-JVM --> Available at *target/HTMLreports/cucumber-html-reports/overview-features.html*


## How to run the test in a CI/CD pipeline
_jenkins.pipeline_ file is available in the project. This file can be used for executing the tests using Jenkins.
There are 3 stages available in this file.
* Git pull --> *To pull the project from github*
* ExecuteTests --> *To execute the tests*

Note: This is a draft feature.
