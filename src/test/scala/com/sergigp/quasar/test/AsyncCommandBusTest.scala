package com.sergigp.quasar.test

import scala.concurrent.ExecutionContext.Implicits.global

import com.sergigp.quasar.command.{AsyncCommandBus, Command}
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.{AddDummyUserCommand, CommandHandlers}
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.DummyUserAlreadyExists
import com.sergigp.quasar.test.stub.{StringStub, UuidStringStub}
import org.scalatest.concurrent.ScalaFutures

class AsyncCommandBusTest extends TestCase {
  "a command bus" should {
    "subscribe two times the same command handler without throwing error" in {
      val commandBus = new AsyncCommandBus(logger)
      commandBus.subscribe[AddDummyUserCommand](CommandHandlers.dummyHandler)
      commandBus.subscribe[AddDummyUserCommand](CommandHandlers.dummyHandler)
    }

    "throw an error if don't find a command handler for a command" in {
      val commandBus = new AsyncCommandBus(logger)

      val result = commandBus.publish(AddDummyUserCommand(UuidStringStub.random, StringStub.random(10)))

      ScalaFutures.whenReady(result.failed) { e =>
        e.getMessage should be ("handler for AddDummyUserCommand not found")
      }
    }
  }

  "a command bus client" should {
    "receive successful result if user is added" in {
      val commandBus = new AsyncCommandBus(logger)

      val userId = UuidStringStub.random
      val userName = StringStub.random(10)
      commandBus.subscribe[AddDummyUserCommand](CommandHandlers.handlerWithService(userAdder, userId, userName))

      shouldAddUser(userId, userName)

      commandBus.publish(AddDummyUserCommand(userId, userName)).futureValue.right.value should be (())
    }

    "compose multiple handlers" in {
      val commandBus = new AsyncCommandBus(logger)

      val userId = UuidStringStub.random
      val userName = StringStub.random(10)

      val handler = (c: AddDummyUserCommand) => {
        for {
          _ <- CommandHandlers.handlerWithService(userAdder, userId, userName)(c)
          b <- CommandHandlers.handlerWithService(userAdder, userId, userName)(c)
        } yield b
      }

      commandBus.subscribe[AddDummyUserCommand](handler)
      shouldAddUser(userId, userName)
      shouldAddUser(userId, userName)

      commandBus.publish(AddDummyUserCommand(userId, userName)).futureValue.right.value should be (())
    }

    "receive error if user already exists" in {
      val commandBus = new AsyncCommandBus(logger)
      val userId = UuidStringStub.random
      val userName = StringStub.random(10)

      commandBus.subscribe[AddDummyUserCommand](CommandHandlers.handlerWithService(userAdder, userId, userName))
      val expectedError = DummyUserAlreadyExists(userId)

      shouldReturnErrorAddingUser(userId, userName, expectedError)

      commandBus.publish(AddDummyUserCommand(userId, userName)).futureValue.left.value should be (expectedError)
    }
  }
}
