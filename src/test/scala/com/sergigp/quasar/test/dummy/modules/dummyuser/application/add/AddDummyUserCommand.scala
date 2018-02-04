package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import com.sergigp.quasar.command.Command
import AddDummyUserError.AddDummyUserError

case class AddDummyUserCommand(id: String, name: String) extends Command {
  override type CommandError = AddDummyUserError
}
