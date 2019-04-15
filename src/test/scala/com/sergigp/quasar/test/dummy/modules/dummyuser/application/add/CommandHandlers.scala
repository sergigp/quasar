package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.add.UserAdder

object CommandHandlers {
  val dummyHandler: AddDummyUserCommand => Future[Unit] = (_: AddDummyUserCommand) => Future.successful(())

  def handlerWithService(userAdder: UserAdder, id: String, name: String): AddDummyUserCommand => Future[Unit] =
    (_: AddDummyUserCommand) => userAdder.add(id, name)
}
