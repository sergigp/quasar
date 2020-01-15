package com.sergigp.quasar.test

import scala.concurrent.ExecutionContext.Implicits.global

import com.sergigp.quasar.command.AsyncCommandBus
import com.sergigp.quasar.query.AsyncQueryBus
import com.sergigp.quasar.test.dummy.modules.dummyuser.DummyUser
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserResponse
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.{AddDummyUserCommand, CommandHandlers}
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.{FindDummyUserQuery, QueryHandlers}
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.FindDummyUserError.DummyUserNotFoundError
import com.sergigp.quasar.test.stub.{StringStub, UuidStringStub}
import org.scalatest.concurrent.ScalaFutures

class AsyncQueryBusTest extends TestCase {
  "a query bus" should {
    "subscribe two times the same query handler without throwing error" in {
      val queryBus = new AsyncQueryBus(logger)
      queryBus.subscribe[FindDummyUserQuery](QueryHandlers.dummyHandler(userFinder))
      queryBus.subscribe[FindDummyUserQuery](QueryHandlers.dummyHandler(userFinder))
    }

    "throw an error if don't find a query handler for a query" in {
      val queryBus = new AsyncQueryBus(logger)

      val result = queryBus.ask(FindDummyUserQuery(UuidStringStub.random))

      ScalaFutures.whenReady(result.failed) { e =>
        e.getMessage should be("handler for FindDummyUserQuery not found")
      }
    }

    "invoke onsuccess hook when success happens" in {
      var calls = List.empty[String]
      val successHandler: String => Unit = { command: String =>
        calls = calls :+ command
      }

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      val queryBus = new AsyncQueryBus(logger, successHandler)
      queryBus.subscribe[FindDummyUserQuery](QueryHandlers.dummyHandler(userFinder))

      shouldFindUser(userId, DummyUser(userId, userName, userEmail))

      queryBus.ask(FindDummyUserQuery(userId)).futureValue.right.value should be(
        DummyUserResponse(userId, userName, userEmail)
      )
      calls should be(List("FindDummyUserQuery"))
    }

    "invoke onfailure hook when failure happens" in {
      var calls = List.empty[String]
      val failedHandler: Throwable => Unit = { error: Throwable =>
        calls = calls :+ error.getMessage
      }

      val userId   = UuidStringStub.random
      val queryBus = new AsyncQueryBus(logger, onFailure = failedHandler)
      queryBus.subscribe[FindDummyUserQuery](QueryHandlers.dummyHandler(userFinder))

      shouldFailFindingUser(userId)

      val result = queryBus.ask(FindDummyUserQuery(userId))

      ScalaFutures.whenReady(result.failed) { e =>
        e.getMessage should be("expected exception")
      }

      eventually {
        calls should be(List("expected exception"))
      }
    }

    "return successful result if user exists" in {
      val queryBus = new AsyncQueryBus(logger)
      queryBus.subscribe[FindDummyUserQuery](QueryHandlers.dummyHandler(userFinder))

      val userId    = UuidStringStub.random
      val userName  = StringStub.random(10)
      val userEmail = StringStub.random(10)

      shouldFindUser(userId, DummyUser(userId, userName, userEmail))

      queryBus.ask(FindDummyUserQuery(userId)).futureValue.right.value should be(
        DummyUserResponse(userId, userName, userEmail)
      )
    }

    "return error if user don't exists" in {
      val queryBus = new AsyncQueryBus(logger)
      queryBus.subscribe[FindDummyUserQuery](QueryHandlers.dummyHandler(userFinder))

      val userId = UuidStringStub.random

      shouldNotFindUser(userId)

      val result = queryBus.ask(FindDummyUserQuery(userId))

      result.futureValue.left.value should be(DummyUserNotFoundError(userId))
    }
  }
}
