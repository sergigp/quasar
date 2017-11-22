<img src="doc/image/quasar.png" align="left" width="128px" height="128px"/>
<img align="left" width="0" height="128px" hspace="10"/>

[![letgo](https://img.shields.io/badge/letgo-quasar-red.svg?style=flat-square)](http://letgo.com)

> /ˈkweɪzɑːr/ Is an active galactic nucleus of very high luminosity :dizzy:

Quasar is a library to do [CQRS](https://martinfowler.com/bliki/CQRS.html) in Scala.
<br> <br>

## Quasar - What is CQRS?
In **CQRS** (Command Query Responsibility Segregation) basically says that an input in a software system can change state of retrieve it but not both.
The artifacts that we use to do CQRS are `Commands` and `Queries` and we send it through `CommandBus` and `QueryBus. At [Letgo](https://www.letgo.com) we use to append the suffix Command or Query to each one as a convention.

Some examples of commands and queries: `AddUserCommand`, `SendMessageCommand`, `UpdateProductPriceCommand`, `FindUserQuery`, `FetchMessagesQuery`, ... 

Commands only modifies state and are not allowed to return the new one. When you publish a command in command bus the response only denotes if state modification has occurred successfully or an error has happened.
Queries only retrieves the state and are not allowed to modify it. When you ask to query bus a query it will return a response with requested state or an error if something happened.

## Async?

Quasar provides two interfaces `CommandBus` and `QueryBus` that accepts a type parameter `P`. Quasar also provides Async implementations of each bus powered by Scala `Future`. So you can take advantage of scala asynchronous mechanisms to ask queries and publish commands! 

## Install

TBD

## Usage

You can visit our [doc](doc/index.md) to learn to use Quasar

## Improvements

logo designed by [FreePik](https://www.flaticon.com/authors/freepik)
