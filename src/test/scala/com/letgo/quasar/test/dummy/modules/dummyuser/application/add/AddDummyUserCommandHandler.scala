package com.letgo.quasar.test.dummy.modules.dummyuser.application.add

import scala.concurrent.Future

import cats.data.Validated.Valid

import com.letgo.quasar.command.CommandHandler
import com.letgo.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError
import com.letgo.quasar.test.dummy.modules.dummyuser.domain.add.UserAdder
import com.letgo.quasar.validation.Validation.Validation

class AddDummyUserCommandHandler(userAdder: UserAdder) extends CommandHandler[Future, AddDummyUserCommand] {

  override def handle(command: AddDummyUserCommand): Validation[Future[Either[AddDummyUserError, Unit]]] =
    Valid(userAdder.add(command.id, command.name))

//  private def validate(command: AddDummyUserCommand) = Valid(command.id) |@| Valid(command.name)
}
