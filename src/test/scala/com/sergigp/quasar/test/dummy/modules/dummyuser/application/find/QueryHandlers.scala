package com.sergigp.quasar.test.dummy.modules.dummyuser.application.find

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserResponse
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.FindDummyUserError.FindDummyUserError
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.find.UserFinder

object QueryHandlers {
  def dummyHandler(
    userFinder: UserFinder
  ): FindDummyUserQuery => Future[Either[FindDummyUserError, DummyUserResponse]] =
    (q: FindDummyUserQuery) =>
      userFinder.find(q.id).map {
        case Right(user) => Right(DummyUserResponse(user.id, user.name, user.email))
        case Left(error) => Left(error)
      }
}
