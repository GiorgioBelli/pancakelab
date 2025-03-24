# Refactoring Improvements

The refactoring of the application primarily focused on the following areas:

- **State Management**: Moved the order state into the order itself, using the **State** pattern to handle state transitions more effectively.
- **Event Notification**: Implemented the **Observer** pattern to notify all relevant entities when specific actions occur in the system, such as status updates and pancake modifications.
- **Concurrency Handling**: Added synchronized blocks and concurrent data structures to prevent potential race conditions.
- **Input Validation**: Improved user input validation to prevent invalid orders and introduced an enumerator for ingredients to ensure only allowed ingredients can be selected.
- **Factory Pattern**: Introduced a **Pancake Factory** to manage pre-configured recipes while maintaining flexibility for future extensions.
- **Order Logging**: The order log now directly subscribes to notifications from the `PancakeService`.
- **Result Object**: The `PancakeService` now returns a **result object** that encapsulates the response data and includes an error message if the operation fails.

## How to Run It

To execute the entire system, an integration test is provided. This test demonstrates how the involved classes interact by simulating the following actors:

- **3 Disciples**
- **1 Chef**
- **1 Delivery Guy**

The test verifies that all order states transition correctly. Additionally, it runs the actors in separate threads to test for concurrency issues.

## Diagrams

- **Class Diagram**: UML representation of the application's class structure.  
  📄 [class_diagram.pdf](class_diagram.pdf)

- **Order Workflow**: UML sequence diagram illustrating how an order is processed from creation to delivery (assuming no errors).  
  📄 [correct_order_workflow.pdf](correct_order_workflow.pdf)  


## Possible TODOs

- Improve the notification system to support groups of subscribers and load balancing
- Improve the repository to add some indexes to enhance order search by status instead of iterating over all the orders
- Add more test cases for edge scenarios (e.g., ingredient limits).

## Possible Future Improvements

- crete a CLI for the actors to interact with the system
- refine logging mechanisms to provide better debugging insights for concurrency issues.