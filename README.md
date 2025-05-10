# workscripts project
Scripts for diagnosis or create test/example environments

# Architecture 
We took a [Swing MVC example](Swing%20MVC%20Example.md) from StackOverflow as a guide, and created the following
architecture:

**Model (Persistence + Business logic / Services)**: This is where the data and business logic resides. 
This layer is responsible for:
- Storing and retrieving data.
- Performing business logic operations.

**View and Controller (Java Swing GUI, user input handling) *: They are in the [](gui) package. 
The view is the GUI, and the controller is the logic that handles user input and 
calls the corresponding classes in the Model layer.