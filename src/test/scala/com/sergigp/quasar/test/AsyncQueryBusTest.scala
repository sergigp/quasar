package com.sergigp.quasar.test

import scala.concurrent.ExecutionContext.Implicits.global

import com.sergigp.quasar.test.dummy.modules.dummyuser.DummyUser
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserResponse
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.{FindDummyUserQuery, FindDummyUserQueryHandler}
import com.sergigp.quasar.test.stub.{StringStub, UuidStringStub}
import org.scalatest.concurrent.ScalaFutures

import com.sergigp.quasar.query.AsyncQueryBus
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.FindDummyUserError.DummyUserNotFoundError

class AsyncQueryBusTest extends TestCase {

  "a query bus" should {
    "subscribe two times the same query handler without throwing error" in {
      val queryBus = new AsyncQueryBus(logger)
      queryBus.subscribe(new FindDummyUserQueryHandler(userFinder))
      queryBus.subscribe(new FindDummyUserQueryHandler(userFinder))
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
      val dummyHandler = new FindDummyUserQueryHandler(userFinder)

      queryBus.subscribe(dummyHandler)

      val userId = UuidStringStub.random
      val userName = StringStub.random(10)

      shouldFindUser(userId, DummyUser(userId, userName))

      queryBus.ask(FindDummyUserQuery(userId)).futureValue.right.value should be (DummyUserResponse(userId, userName))
    }

    "receive error if user don't exists" in {
      val queryBus = new AsyncQueryBus(logger)
      val dummyHandler = new FindDummyUserQueryHandler(userFinder)

      queryBus.subscribe(dummyHandler)

      val userId = UuidStringStub.random

      shouldNotFindUser(userId)

      queryBus.ask(FindDummyUserQuery(userId)).futureValue.left.value should be (DummyUserNotFoundError(userId))
    }
  }
}
