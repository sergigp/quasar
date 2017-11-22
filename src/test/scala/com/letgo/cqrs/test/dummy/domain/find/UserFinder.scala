package com.letgo.cqrs.test.dummy.domain.find

import scala.concurrent.Future

import com.letgo.cqrs.test.dummy.DummyUser
import com.letgo.cqrs.test.dummy.application.FindDummyUserError.FindDummyUserError

trait UserFinder {
  def find(id: String): Future[Either[FindDummyUserError, DummyUser]]
}
