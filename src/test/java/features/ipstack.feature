@ipstack @api
Feature: Verify ipStack endpoint

  Background:
    Given I use "http://api.ipstack.com" as base URI

  @ipstack1
  Scenario: Standard IP Lookup - Verify Standard IP Lookup functionality
    When I make "GET" call to "/134.201.250.155?access_key=9e8c7a309366f4a2d05ed7f82d96eb66"
    Then I get response code "200"
    And I verify response should match with contract file "standardIpLookupContract"

  @ipstack2
  Scenario: Requester IP Lookup - Verify Requester IP Lookup functionality
    When I make "GET" call to "/check?access_key=9e8c7a309366f4a2d05ed7f82d96eb66"
    Then I get response code "200"
    And I verify response should match with contract file "standardIpLookupContract"

  @ipstack3
  Scenario Outline: Options - <RESPONSE_TYPE> - Verify options functionality
    When I make "GET" call to "/134.201.250.155?access_key=9e8c7a309366f4a2d05ed7f82d96eb66&output=<RESPONSE_TYPE>"
    Then I get response code "200"
    And I verify the response is in "<RESPONSE_TYPE>" format
    Examples:
      | RESPONSE_TYPE |
      | json          |
      | xml           |

  @ipstack4
  Scenario Outline: Response Fields - <FIELD> - Verify Response Fields functionality
    When I make "GET" call to "/134.201.250.155?access_key=9e8c7a309366f4a2d05ed7f82d96eb66&fields=<FIELD>"
    Then I get response code "200"
    And I verify the response contains only "<FIELDS>"
    Examples:
      | FIELD                         | FIELDS                                                                                                              |
      | main                          | ip,type,continent_code,continent_name,country_code,country_name,region_code,region_name,city,zip,latitude,longitude |
      | country_code                  | country_code                                                                                                        |
      | location                      | location                                                                                                            |
      | location.capital              | location.capital                                                                                                    |
      | country_code,location.capital | country_code,location.capital                                                                                       |
