package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import scala.concurrent.Future

import cats.data.Validated.Valid
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.add.UserAdder

import com.sergigp.quasar.command.CommandHandler
import com.sergigp.quasar.validation.Validation.Validation

class AddDummyUserCommandHandler(userAdder: UserAdder) extends CommandHandler[Future, AddDummyUserCommand] {

  override def handle(command: AddDummyUserCommand): Validation[Future[Either[AddDummyUserError, Unit]]] =
    Valid(userAdder.add(command.id, command.name))

//  private def validate(command: AddDummyUserCommand) = Valid(command.id) |@| Valid(command.name)
}
