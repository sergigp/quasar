# DDD concepts

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

##Module

TBD
