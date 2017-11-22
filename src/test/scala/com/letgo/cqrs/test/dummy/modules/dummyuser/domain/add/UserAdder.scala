package com.letgo.cqrs.test.dummy.modules.dummyuser.domain.add

import scala.concurrent.Future

import com.letgo.cqrs.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError

trait UserAdder {
  def add(id: String, name: String): Future[Either[AddDummyUserError, Unit]]
}
