package com.sergigp.quasar.test

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.sergigp.quasar.query.{AsyncQueryBus, Query}
import com.sergigp.quasar.test.dummy.modules.dummyuser.DummyUser
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserResponse
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
        e.getMessage should be ("handler for FindDummyUserQuery not found")
      }
    }
  }

  "a query bus client" should {
    "receive successful result if user exists" in {
      val queryBus = new AsyncQueryBus(logger)
      queryBus.subscribe[FindDummyUserQuery](QueryHandlers.dummyHandler(userFinder))

      val userId = UuidStringStub.random
      val userName = StringStub.random(10)

      shouldFindUser(userId, DummyUser(userId, userName))

      queryBus.ask(FindDummyUserQuery(userId)).futureValue.right.value should be (DummyUserResponse(userId, userName))
    }

    "receive error if user don't exists" in {
      val queryBus = new AsyncQueryBus(logger)
      queryBus.subscribe[FindDummyUserQuery](QueryHandlers.dummyHandler(userFinder))

      val userId = UuidStringStub.random

      shouldNotFindUser(userId)

      val result = queryBus.ask(FindDummyUserQuery(userId))

      result.futureValue.left.value should be (DummyUserNotFoundError(userId))
    }
  }
}
