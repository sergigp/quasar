package com.letgo.cqrs.test.dummy.modules.dummyuser.application.find

import com.letgo.cqrs.test.dummy.modules.dummyuser.application.DummyUserModuleError

object FindDummyUserError {
  sealed abstract class FindDummyUserError extends DummyUserModuleError
  case class DummyUserNotFoundError(id: String) extends FindDummyUserError
}
