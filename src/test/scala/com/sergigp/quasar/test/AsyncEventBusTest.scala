package com.sergigp.quasar.test

import scala.concurrent.ExecutionContext.Implicits.global

import com.sergigp.quasar.event.AsyncEventBus
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.EventHandlers
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.event.UserCreatedDomainEvent
import com.sergigp.quasar.test.stub.{StringStub, UuidStringStub}
import org.scalatest.concurrent.ScalaFutures

class AsyncEventBusTest extends TestCase {
  "a event bus" should {
    "subscribe two times the same event handler without throwing error" in {
      val eventBus = new AsyncEventBus(logger)
      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.dummyHandler)
      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.dummyHandler)
    }

    "don't throw an error if don't find a event handler for an event" in {
      var calls = List.empty[String]
      val notFoundHandler: String => Unit = { event: String =>
        calls = calls :+ event
      }
      val eventBus = new AsyncEventBus(logger, onHandlerNotFound = notFoundHandler)

      val result = eventBus.publish(
        UserCreatedDomainEvent(UuidStringStub.random, StringStub.random(10), StringStub.random(10))
      )

      result.futureValue should be (())
      calls should be(List("UserCreatedDomainEvent"))
    }

    "receive successful result if user is created" in {
      val eventBus = new AsyncEventBus(logger)

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithService(emailSender))

      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())
    }

    "compose multiple handlers" in {
      val eventBus = new AsyncEventBus(logger)

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      val handler = (e: UserCreatedDomainEvent) => {
        for {
          _ <- EventHandlers.handlerWithService(emailSender)(e)
          b <- EventHandlers.handlerWithService(emailSender)(e)
        } yield b
      }

      eventBus.subscribe[UserCreatedDomainEvent](handler)
      shouldSendEmail(userEmail)
      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())
    }

    "receive error can't send email" in {
      val eventBus  = new AsyncEventBus(logger)
      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithService(emailSender))

      val expectedError = new RuntimeException("expected error")

      shouldReturnErrorSendingEmail(userEmail, expectedError)

      val result = eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail))

      ScalaFutures.whenReady(result.failed) { e =>
        e should be(expectedError)
      }
    }

    "invoke onsuccess hook when success happens" in {
      var calls = List.empty[String]
      val successHandler: String => Unit = { event: String =>
        calls = calls :+ event
      }
      val eventBus = new AsyncEventBus(logger, onSuccess = successHandler)

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithService(emailSender))

      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())

      calls should be(List("UserCreatedDomainEvent"))
    }

    "invoke onfailure hook when failure happens" in {
      var calls = List.empty[String]
      val failedHandler: Throwable => Unit = { error: Throwable =>
        calls = calls :+ error.getMessage
      }
      val eventBus = new AsyncEventBus(logger, onFailure = failedHandler)

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithService(emailSender))

      shouldFailSendingEmail(userEmail)

      val result = eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail))

      ScalaFutures.whenReady(result.failed) { e =>
        e.getMessage should be("expected exception")
      }

      eventually {
        calls should be(List("expected exception"))
      }
    }
  }
}
