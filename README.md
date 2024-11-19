# Hubspot Work Sample - API calls and JSON manipulation

## Problem
The problem description is provided in the ProblemStatement folder. You can check it to understand the problem.

## Endpoints 
HubSpot GET Endpoint: `https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=${USER_KEY}`
HubSpot POST Endpoint: `https://candidate.hubteam.com/candidateTest/v3/problem/result?userKey=${USER_KEY}`

In our project, user key is configured in application.properties and used using CustomAppProperties class within our code. Usually user key needs to be hidden but this is a demo project, so using it as it is.

The above endpoints are reached indirectly through our rest controller (HubSpotApiController) internally.
Our controller can be reached by hitting `http://localhost:8080/hubspot-api/fetch-data` when you run the project locally. 

## Requirements 

Java 17, 
Apache maven 3.9.9

## Libraries
okHTTP -> For making http requests and getting responses.
Jackson-databind -> For data serialization and deserialization.

# Installation and Running
Get the requirements installed and clone this repository.
Run the project by running the following command in the assessments directory: 
`mvn spring-boot:run`

# Sample Data from HubSpot GET endpoint

{
    "callRecords": [
        {
            "customerId": 20906,
            "callId": "4de68d65-7410-4399-8d6f-ea44d6db4466",
            "startTimestamp": 1705663500000,
            "endTimestamp": 1705670160000
        },
        {
            "customerId": 19648,
            "callId": "aeae9b0c-d7af-435d-99ee-2bee78a475c4",
            "startTimestamp": 1705686840000,
            "endTimestamp": 1705689000000
        },
        {
            "customerId": 20906,
            "callId": "8af48b32-4559-4b93-baee-eda2597a0b69",
            "startTimestamp": 1705479060000,
            "endTimestamp": 1705481760000
        }
    ]
}

# Sample Data to send for HubSpot POST endpoint
{
  "results" : [ {
    "customerId" : 19648,
    "date" : "2024-01-15",
    "maxConcurrentCalls" : 8,
    "timestamp" : 1705306440000,
    "callIds" : [ "e1b35ef0-7892-4694-9034-9405a830492c", "9a843d94-b8f7-42b6-a27c-820c69a3a2f9", "d7350c08-c9f4-45c2-9a61-3cac3e4526da", "f3bdabc4-0c54-4e95-8abc-530cfee5a3fd", "2f5ab5f9-b799-4484-ac64-0b36a98b20fd", "d62df1a5-9f9d-4526-a31d-e525b496edeb", "faf04aa3-83f2-43af-bbba-a5ba6b042687", "976fc7a3-e1a6-4873-b5cb-aca8e36f9e04" ]
  }, {
    "customerId" : 19648,
    "date" : "2024-01-17",
    "maxConcurrentCalls" : 8,
    "timestamp" : 1705483620000,
    "callIds" : [ "4c7f1bee-5280-4948-b00d-4c9b2f4c2c62", "b2237f1b-10ab-4c52-9ca8-4f2aa4d8b624", "64027789-a381-4f9e-ad0d-e14d2684b9fa", "0c49e26b-f507-4bac-8b93-ee4709faecbc", "901f0c42-3c03-490c-acb6-1b7e1a239149", "21a055e4-4fac-449b-bd33-c0a58567710d", "9099e006-262d-4ff9-acb9-ac8e3704ed4c", "335a14c8-5315-4963-961d-87b4f372a49d" ]
  }, {
    "customerId" : 32484,
    "date" : "2024-01-09",
    "maxConcurrentCalls" : 2,
    "timestamp" : 1704787320000,
    "callIds" : [ "bad8183b-b936-4c6d-9e07-e29afeaacee7", "fae27d32-ee30-4c81-b68d-6da6663b8e8c" ]
  }]
}

# Success screenshots
When you run your project and then hit the get endpoint to controller, we see a 200 response.
<img width="1680" alt="GET_Call_to_Controller" src="https://github.com/user-attachments/assets/c528f0b3-f896-4905-9287-8d2c01796083">


When the project internally calls the GET endpoint of Hubspot api, gets the records, transforms them and then hits the POST endpoint with the final data, we see a success message 'Results submitted successfully!' at the end indicating a 200 response.
<img width="1680" alt="POST_data_with_success_message" src="https://github.com/user-attachments/assets/d136f89b-471f-4bfd-ad22-fdb2de32c991">

