package com.sergigp.quasar.test

import scala.concurrent.ExecutionContext.Implicits.global

import com.sergigp.quasar.event.AsyncEventBus
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.EventHandlers
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.event.UserCreatedDomainEvent
import com.sergigp.quasar.test.stub.{StringStub, UuidStringStub}

class AsyncEventBusTest extends TestCase {
  "a event bus" should {
    "receive successful result if user is created" in {
      val eventBus = new AsyncEventBus()

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithEmailSender(emailSender))

      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())
    }

    "don't get an error even if can't send email" in {
      val eventBus  = new AsyncEventBus()
      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithEmailSender(emailSender))
      val expectedError = new RuntimeException("expected error")
      shouldReturnErrorSendingEmail(userEmail, expectedError)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())
    }

    "subscribe multiple handlers" in {
      val eventBus = new AsyncEventBus()

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      val handlerOne = EventHandlers.handlerWithEmailSender(emailSender)
      val handlerTwo = EventHandlers.handlerWithEmailSender(emailSender)

      eventBus.subscribe[UserCreatedDomainEvent](handlerOne)
      eventBus.subscribe[UserCreatedDomainEvent](handlerTwo)
      shouldSendEmail(userEmail)
      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())
    }

    "subscribe multiple handlers and return success even if one of them fails" in {
      val eventBus = new AsyncEventBus()

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      val handlerOne = EventHandlers.handlerWithPushSender(pushSender)
      val handlerTwo = EventHandlers.handlerWithEmailSender(emailSender)

      val expectedError = new RuntimeException("expected error")
      eventBus.subscribe[UserCreatedDomainEvent](handlerOne)
      eventBus.subscribe[UserCreatedDomainEvent](handlerTwo)
      shouldReturnErrorSendingPush(userId, expectedError)
      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())
    }

    "invoke onsuccess hook when success happens" in {
      var calls = List.empty[String]
      val successHandler: String => Unit = { event: String =>
        calls = calls :+ event
      }
      val eventBus = new AsyncEventBus(successHandler)

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithEmailSender(emailSender))

      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())

      calls should be(List("UserCreatedDomainEvent"))
    }

    "invoke recordLatencyInMillis hook when success happens" in {
      var calls = List.empty[String]

      def recordLatencyInMillis(name: String, now: Long, after: Long): Unit =
        calls = calls :+ name

      val eventBus = new AsyncEventBus(recordLatencyInMillis = recordLatencyInMillis)

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithEmailSender(emailSender))

      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())

      calls should be(List("UserCreatedDomainEvent"))
    }

    "invoke onfailure hook when failure happens" in {
      var calls = List.empty[String]
      val failedHandler: Throwable => Unit = { error: Throwable =>
        calls = calls :+ error.getMessage
      }
      val eventBus = new AsyncEventBus(onFailure = failedHandler)

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      eventBus.subscribe[UserCreatedDomainEvent](EventHandlers.handlerWithEmailSender(emailSender))

      shouldFailSendingEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())

      eventually {
        calls should be(List("expected exception"))
      }
    }

    "invoke onfailure hook when failure happens even if one handler has success" in {
      var calls = List.empty[String]
      val onFailure: Throwable => Unit = { error: Throwable =>
        calls = calls :+ error.getMessage
      }
      val eventBus = new AsyncEventBus(onFailure = onFailure)

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      val handlerOne = EventHandlers.handlerWithPushSender(pushSender)
      val handlerTwo = EventHandlers.handlerWithEmailSender(emailSender)

      eventBus.subscribe[UserCreatedDomainEvent](handlerOne)
      eventBus.subscribe[UserCreatedDomainEvent](handlerTwo)

      shouldReturnErrorSendingPush(userId, new RuntimeException("expected error"))
      shouldSendEmail(userEmail)

      eventBus.publish(UserCreatedDomainEvent(userId, userName, userEmail)).futureValue should be(())

      eventually {
        calls should be(List("expected error"))
      }
    }
  }
}
