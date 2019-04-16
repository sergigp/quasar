package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.add.UserAdder

object CommandHandlers {
  val dummyHandler: AddDummyUserCommand => Future[Either[AddDummyUserError, Unit]] =
    (_: AddDummyUserCommand) => Future.successful(Right(()))

  def handlerWithService(
    userAdder: UserAdder,
    id: String, name: String
  ): AddDummyUserCommand => Future[Either[AddDummyUserError, Unit]] =
    (_: AddDummyUserCommand) => userAdder.add(id, name)
}
