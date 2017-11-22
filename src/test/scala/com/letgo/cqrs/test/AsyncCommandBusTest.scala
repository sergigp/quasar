package com.letgo.cqrs.test

import org.scalatest.concurrent.ScalaFutures

import com.letgo.cqrs.command.AsyncCommandBus
import com.letgo.cqrs.test.dummy.modules.dummyuser.application.add.{AddDummyUserCommand, AddDummyUserCommandHandler}
import com.letgo.cqrs.test.dummy.modules.dummyuser.application.add.AddDummyUserError.DummyUserAlreadyExists
import com.letgo.cqrs.test.stub.{StringStub, UuidStringStub}

class AsyncCommandBusTest extends TestCase {
  "a command bus" should {
    "subscribe two times the same command handler without throwing error" in {
      val commandBus = new AsyncCommandBus(logger)
      commandBus.subscribe(new AddDummyUserCommandHandler(userAdder))
      commandBus.subscribe(new AddDummyUserCommandHandler(userAdder))
    }

    "throw an error if don't find a command handler for a command" in {
      val commandBus = new AsyncCommandBus(logger)

      val result = commandBus.publish(AddDummyUserCommand(UuidStringStub.random, StringStub.random(10)))

      ScalaFutures.whenReady(result.failed) { e =>
        e.getMessage should be ("handler for AddDummyUserCommand not found")
      }
    }
  }

  "a query bus client" should {
    "receive successful result if user is added" in {
      val commandBus = new AsyncCommandBus(logger)
      commandBus.subscribe(new AddDummyUserCommandHandler(userAdder))

      val userId = UuidStringStub.random
      val userName = StringStub.random(10)

      shouldAddUser(userId, userName)

      commandBus.publish(AddDummyUserCommand(userId, userName)).futureValue.right.value should be (())
    }

    "receive error if user already exists" in {
      val commandBus = new AsyncCommandBus(logger)
      commandBus.subscribe(new AddDummyUserCommandHandler(userAdder))

      val userId = UuidStringStub.random
      val userName = StringStub.random(10)
      val expectedError = DummyUserAlreadyExists(userId)

      shouldReturnErrorAddingUser(userId, userName, expectedError)

      commandBus.publish(AddDummyUserCommand(userId, userName)).futureValue.left.value should be (expectedError)
    }
  }
}
