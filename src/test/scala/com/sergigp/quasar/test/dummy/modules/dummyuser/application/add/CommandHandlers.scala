package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.add.UserAdder

object CommandHandlers {
  val dummyHandler: AddDummyUserCommand => Future[Either[AddDummyUserError, Unit]] =
    (_: AddDummyUserCommand) => Future.successful(Right(()))

  val dummyFailingHandler: AddDummyUserCommand => Future[Either[AddDummyUserError, Unit]] =
    (_: AddDummyUserCommand) => Future.failed(new RuntimeException("expected exception"))

  def handlerWithService(
    userAdder: UserAdder
  ): AddDummyUserCommand => Future[Either[AddDummyUserError, Unit]] =
    (e: AddDummyUserCommand) => userAdder.add(e.id, e.name, e.email)
}
