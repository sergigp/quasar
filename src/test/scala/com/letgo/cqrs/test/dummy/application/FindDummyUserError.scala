package com.letgo.cqrs.test.dummy.application

import com.letgo.cqrs.error.DomainError

object FindDummyUserError {
  sealed abstract class FindDummyUserError extends DomainError
  case class DummyUserNotFoundError(id: String) extends FindDummyUserError
}
