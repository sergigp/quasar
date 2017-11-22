package com.letgo.cqrs.test.dummy.modules.dummyuser.application.find

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import cats.data.Validated.Valid

import com.letgo.cqrs.query.QueryHandler
import com.letgo.cqrs.test.dummy.modules.dummyuser.application.DummyUserResponse
import FindDummyUserError.FindDummyUserError
import com.letgo.cqrs.test.dummy.modules.dummyuser.domain.find.UserFinder
import com.letgo.cqrs.validation.Validation.Validation

class FindDummyUserQueryHandler(userFinder: UserFinder) extends QueryHandler[Future, FindDummyUserQuery] {
  override def handle(
    query: FindDummyUserQuery
  ): Validation[Future[Either[FindDummyUserError, DummyUserResponse]]] = {
    Valid(query.id).map { userId =>
      userFinder.find(userId).map(_.map(user => DummyUserResponse(user.id, user.name)))
    }
  }
}
