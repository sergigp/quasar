package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import com.sergigp.quasar.command.Command
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError

case class AddDummyUserCommand(id: String, name: String) extends Command {
  override type CommandError = AddDummyUserError
}
