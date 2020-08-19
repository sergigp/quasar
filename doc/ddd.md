# Domain Driven Design (DDD) concepts

We won't enter too much in the theory but We will explain briefly some concepts in DDD that fore sure you already know wit other names.

## Value Object
A Value Object (VO) is an object that means something at domain level and its identity is defined by its value. The typical example is the object `Currency`
```
case class Currency(code: String)
```
We can have an instance Currency object with code "USD". If we change this value to "EUR" the identity of the object has changes. Some other VO can be `ProductId`, `UserId` or `Price`, `Email`.

## Aggregate Root
An Aggregate Root (Aggregate, AR) is what we always have known as entity. An Aggregate is composed by Value Objects and primitive types.
```
case class User(id: UserId, name: String, email: Email)
```
Take account that the identity of a `User` it's defined by it's id and not by the rest of its attributes. If you take an instance of a `User` and change its email, the identity of the user it's still the same.

## Module

Ah module is a bunch of code that relates to an specific concept in the business model of our app. Some examples of modules can be `User`, `Invoice`, `Messages`, ...

The communication between modules will be made though command bus and query bus (and event bus) in order to decouple logic. 
This strategy will help us to scale our codebase. If our `User` module grows a lot and we take the decission to isolate all the logic in another microservice we should not have to change a lot of our app because de loose coupling of all our business logic with `User` module.
