package com.sergigp.quasar.test.dummy.modules.dummyuser.domain.add

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError

trait UserAdder {
  def add(id: String, name: String, email: String): Future[Either[AddDummyUserError, Unit]]
}
