package com.letgo.cqrs.test.dummy.modules.dummyuser.application.add

import com.letgo.cqrs.command.Command
import com.letgo.cqrs.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError

case class AddDummyUserCommand(id: String, name: String) extends Command {
  override type CommandError = AddDummyUserError
}
