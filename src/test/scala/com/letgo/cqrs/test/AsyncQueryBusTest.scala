package com.letgo.cqrs.test

import org.scalatest.concurrent.ScalaFutures

import com.letgo.cqrs.query.AsyncQueryBus
import com.letgo.cqrs.test.dummy.DummyUser
import com.letgo.cqrs.test.dummy.application.DummyUserResponse
import com.letgo.cqrs.test.dummy.application.find.{FindDummyUserQueryHandler, FindUserDummyQuery}
import com.letgo.cqrs.test.dummy.application.FindDummyUserError.DummyUserNotFoundError
import com.letgo.cqrs.test.stub.{StringStub, UuidStringStub}

class AsyncQueryBusTest extends TestCase {

  "A query bus" should {
    "subscribe two times the same query handler without throwing error" in {
      val queryBus = new AsyncQueryBus(logger)
      queryBus.subscribe(new FindDummyUserQueryHandler(userFinder))
      queryBus.subscribe(new FindDummyUserQueryHandler(userFinder))
    }

    "throw an error if don't find a query handler for a query" in {
      val queryBus = new AsyncQueryBus(logger)

      val result = queryBus.ask(FindUserDummyQuery(UuidStringStub.random))

      ScalaFutures.whenReady(result.failed) { e =>
        e.getMessage should be ("handler for FindUserDummyQuery not found")
      }
    }
  }

  "A query bus client" should {
    "receive successful result if user exists" in {
      val queryBus = new AsyncQueryBus(logger)
      val dummyHandler = new FindDummyUserQueryHandler(userFinder)

      queryBus.subscribe(dummyHandler)

      val userId = UuidStringStub.random
      val userName = StringStub.random(10)

      shouldFindUser(userId, DummyUser(userId, userName))

      queryBus.ask(FindUserDummyQuery(userId)).futureValue.right.value should be (DummyUserResponse(userId, userName))
    }

    "receive error if user don't exists" in {
      val queryBus = new AsyncQueryBus(logger)
      val dummyHandler = new FindDummyUserQueryHandler(userFinder)

      queryBus.subscribe(dummyHandler)

      val userId = UuidStringStub.random

      shouldNotFindUser(userId)

      queryBus.ask(FindUserDummyQuery(userId)).futureValue.left.value should be (DummyUserNotFoundError(userId))
    }
  }
}
