Feature: Get all cities
  As a user
  I want to be able to create, find, delete and update cities

  Scenario: Get all the cities from the database
  Any user need to be able to retrieve all the register cities
    Given we have a list of cities provided in a "City - Test Data.xlsx"
    And the cities are saved in the database
    When we ask to retrieve all registered cities
    Then the first page of register cities will be in the response body
    And the pagination data like total number of cities, total number of pages and cities per page will be in the response header
