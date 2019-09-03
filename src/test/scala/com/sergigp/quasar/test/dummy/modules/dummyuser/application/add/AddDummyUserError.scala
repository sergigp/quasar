package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserModuleError

object AddDummyUserError {
  sealed abstract class AddDummyUserError       extends DummyUserModuleError
  case class DummyUserAlreadyExists(id: String) extends AddDummyUserError
}
