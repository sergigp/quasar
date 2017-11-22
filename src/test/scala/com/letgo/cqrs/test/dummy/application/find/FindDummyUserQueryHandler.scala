package com.letgo.cqrs.test.dummy.application.find

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import cats.data.Validated.Valid

import com.letgo.cqrs.query.QueryHandler
import com.letgo.cqrs.test.dummy.application.DummyUserResponse
import com.letgo.cqrs.test.dummy.application.FindDummyUserError.FindDummyUserError
import com.letgo.cqrs.test.dummy.domain.find.UserFinder
import com.letgo.cqrs.validation.Validation.Validation

class FindDummyUserQueryHandler(userFinder: UserFinder) extends QueryHandler[Future, FindUserDummyQuery] {
  override def handle(
    query: FindUserDummyQuery
  ): Validation[Future[Either[FindDummyUserError, DummyUserResponse]]] = {
    Valid(query.id).map { userId =>
      userFinder.find(userId).map(_.map(user => DummyUserResponse(user.id, user.name)))
    }
  }
}
