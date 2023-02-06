# Coding Challenge Solution - Ethan Garry

## TASK 1

### Solution Overview

For creating the reporting structure map I used a recursive Depth First Search approach. The instructions were vague, so I returned
all objects in the ReportingStruct class object (similar to how employees are returned) - the requested employee, numberOfReports, and a Map object
of the reporting structure.

I debated including the reportingStructure in the employee controller/service. However, since the prompt asked
for a specific type, along with a new field, it made more sense to match the employee pattern. This approach makes
reporting structure more extensible in the future. Normally, I would consult with the team on this design decision
if it were more impactful.

### The Endpoint

Run `gradlew bootRun` and make a request to localhost:8080/reportingStructure/{employeeId}

```
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/reportingStructure/{id}
    * RESPONSE: ReportingStructure
```

### Testing and Coverage

I followed the EmployeeServiceImplTest implementation, which is a hybrid unit/integration test
approach. Therefore, I didn't write explicit unit tests for "processDirectReports" and "getEmployeeName" 
since they are covered in the "testReportingStructureServiceWithGoodInput" test. In a more robust
project, we might see more narrowly defined unit tests on the function level and a separate integration
test.

Test Coverage (lines):

ReportingStructureController: 100% <br />
ReportingStructure: 100% <br />
CircularDependencyException: 100% <br />
ReportingStructureServiceImpl: 90% (see Circular Dependency below)

100% for all methods

### Edge Cases

Edge Case: First and/or last name missing
The best practice for handling such a scenario is to return a custom text replacement like "No name available for employeeId xxx", 
but as always, best practices are up for debate. Throwing an exception can lead to unexpected behavior and break the flow of 
the application, while returning a null value may not clearly convey the meaning or reason behind the missing information. A custom 
message allows for clear communication of the issue to the user while still preserving the structure of the response data. Since there 
is no clear business requirement outlined in the prompt, I used the custom text replacement if either first or last name was missing.

Edge Case: Circular Reporting Structure
In this case, we want the application to fail. However, we want to give the end user and application owners as much information as possible,
so we'll throw a custom exception and log the error. Interestingly, I was unable to programmatically test this functionality due to a Jackson 
exception when creating the dependency structure in the set-up:

"Could not write JSON: Infinite recursion (StackOverflowError); nested exception is com.fasterxml.jackson.databind.JsonMappingException: Infinite recursion (StackOverflowError)..."

I didn't deep dive on this, but assume it is a preventative measure to deter programmatic circular dependency issues. However, I left the 
functionality there in case someone manually edits data and accidentally creates a circular dependency. If this were a
"real" project I would consult with the team on requirements. I was able to test the exception class itself in 
normal unit testing fashion.

## TASK 2

### Solution Overview

Similar to the employee service, I've created controller, DAO, data, and service classes 
for the compensation type. The difference between reportingStructure and compensation is 
the persistence of data. Therefore, we have a Data Access Object for compensation and not
reporting structure.

There were many directions to go in for Salary. For a "real" project, we're probably making
a custom class considering the specific business requirements. Since there were none for this
project, I stuck with the simple approach - integers. I assumed we were not considering hourly
rates and since salaries are usually positive whole numbers, an integer made logical sense. We also
could have simply made Salary a string to capture all cases, but would decrease the extensibility
of our project - further operations on Salary would become more strenuous.

I used a java.time.LocalDate object for Date. I did however notice some warning messages:
"o.s.data.convert.CustomConversions: Registering converter from class java.time.LocalDateTime to class java.time.Instant as reading converter although it doesn't convert from a store-supported type! You might wanna check you annotation setup at the converter implementation."

I tested the endpoint in POSTMAN and had no issues creating and reading a compensation object. I didn't deep dive further on the warnings,
but we should note it and mark it as an item to investigate in the future.

### The Endpoint(s)

Run `gradlew bootRun`

```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/compensation
    * PAYLOAD: Compensation 
    * RESPONSE: Compensation 

* READ
    * HTTP Method: GET
    * URL: localhost:8080/compensation/{id}    
    * RESPONSE: Compensation
```

### Testing and Coverage

Used the same strategy as Employee type.

Test Coverage (lines):

CompensationController: 100% <br />
Compensation: 100% <br />
CompensationServiceImpl: 100% <br /> 

100% for all methods

There aren't really edge cases to consider here - we handle bad input data the same as employee service.

## Future Improvements

1. As of now, you can only read a compensation object if you know a compensation id. But let's say you want to 
know the compensation for a specific employee. We should add a compensationId
field to the employee type so that compensation is an easy lookup once you have a user.

2. Add a bad case test to the employee service to capture the exception in the read function.

# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped 
with data. The application contains information about all employees at a company. On application start-up, an in-memory 
Mongo database is bootstrapped with a serialized snapshot of the database. While the application runs, the data may be
accessed and mutated in the database without impacting the snapshot.

### How to Run
The application may be executed by running `gradlew bootRun`.

### How to Use
The following endpoints are available to use:

```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
For all endpoints that require an "id" in the URL, this is the "employeeId" field.

## What to Implement
Clone or download the repository, do not fork it.

### Task 1
Create a new type, ReportingStructure, that has two properties: employee and numberOfReports.

For the field "numberOfReports", this should equal the total number of reports under a given employee. The number of 
reports is determined to be the number of directReports for an employee and all of their distinct reports. For example, 
given the following employee structure:
```
                    John Lennon
                /               \
         Paul McCartney         Ringo Starr
                               /        \
                          Pete Best     George Harrison
```
The numberOfReports for employee John Lennon (employeeId: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4. 

This new type should have a new REST endpoint created for it. This new endpoint should accept an employeeId and return 
the fully filled out ReportingStructure for the specified employeeId. The values should be computed on the fly and will 
not be persisted.

### Task 2
Create a new type, Compensation. A Compensation has the following fields: employee, salary, and effectiveDate. Create 
two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
Compensation from the persistence layer.

## Delivery
Please upload your results to a publicly accessible Git repo. Free ones are provided by Github and Bitbucket.
