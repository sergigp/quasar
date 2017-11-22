package com.letgo.quasar.test.dummy.modules.dummyuser.application.add

import com.letgo.quasar.test.dummy.modules.dummyuser.application.DummyUserModuleError

object AddDummyUserError {
  sealed abstract class AddDummyUserError extends DummyUserModuleError
  case class DummyUserAlreadyExists(id: String) extends AddDummyUserError
}
