package com.sergigp.quasar.test.dummy.modules.dummyuser.domain.find

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.DummyUser

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.FindDummyUserError.FindDummyUserError

trait UserFinder {
  def find(id: String): Future[Either[FindDummyUserError, DummyUser]]
}
