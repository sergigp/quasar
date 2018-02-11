# CQRS concepts

In **CQRS** (Command Query Responsibility Segregation) basically says that an input in a software system can change state of retrieve it but not both.

## Command
The artifacts that we use to modify the state in CQRS are called `Commands`. 
Commands only modifies state and are not allowed to return the new one. When you publish a command in command bus the response only denotes if state modification has occurred successfully or an error has happened.
We send commands through `CommandBus`. We suggest append the suffix Command on each one as a convention.

Some examples of commands: `AddUserCommand`, `SendMessageCommand`, `UpdateProductPriceCommand`, ... 

## Queries
The artifacts that we use to retrieve the state in CQRS are called `Queries`.
Queries only retrieves the state and are not allowed to modify it. When you ask to query bus a query it will return a response with requested state or an error if something happened.
We send commands through `QueryBus`. We suggest append the suffix Query on each one as a convention.

Some examples of queries: `FindUserQuery`, `FetchMessagesQuery`, ... 

## Response
A `Response` is what `QueryBus` returns us when a query is asked. 
It's important to force us to do not include domain objects in responses. Our recommendation is to only use scalar data types in `Responses` with some exceptions (DateTime, etc). The main goal of this "rule" is to not couple modules [(explained in DDD section)](ddd.md).

Some examples of responses: `UserResponse`, `ConversationMessagesResponse`, ... 
