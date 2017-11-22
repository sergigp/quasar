package com.letgo.quasar.test.dummy.modules.dummyuser.application.add

import com.letgo.quasar.command.Command
import com.letgo.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError

case class AddDummyUserCommand(id: String, name: String) extends Command {
  override type CommandError = AddDummyUserError
}
