# workscripts project
Scripts for diagnosis or create test/example environments

# Architecture 
This is really a Client - Server architecture, where the server is a REST API,
and the client is a Java Swing GUI, or a Web client.

**Model (Persistence + Business logic / Services)**: This is where the data and business logic resides. 
This layer is responsible for:
- Storing and retrieving data.
- Performing business logic operations.

**Controller (for REST access)**
  - The controller is responsible for handling HTTP requests and responses. 
  - It interacts with the model to perform operations and returns the results to the client.

**View 1 (Java Swing GUI, user input handling) *: They are in the [](gui) package. 
- There are 2 views planned
  - 1. Java Swing GUI
  - 2. Web

For the Java GUI, ee took a [Swing MVC example](Swing%20MVC%20Example.md) from StackOverflow as a guide

