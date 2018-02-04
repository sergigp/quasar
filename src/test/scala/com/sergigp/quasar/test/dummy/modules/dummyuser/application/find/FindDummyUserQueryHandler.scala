package com.sergigp.quasar.test.dummy.modules.dummyuser.application.find

import scala.concurrent.{ExecutionContext, Future}

import cats.data.Validated.Valid
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserResponse
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.FindDummyUserError.FindDummyUserError
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.find.UserFinder

import com.sergigp.quasar.query.QueryHandler
import com.sergigp.quasar.validation.Validation.Validation

class FindDummyUserQueryHandler(userFinder: UserFinder)(implicit ec: ExecutionContext)
  extends QueryHandler[Future, FindDummyUserQuery] {
  override def handle(
    query: FindDummyUserQuery
  ): Validation[Future[Either[FindDummyUserError, DummyUserResponse]]] =
    Valid(query.id).map { userId =>
      userFinder.find(userId).map(_.map(user => DummyUserResponse(user.id, user.name)))
    }
}
