package com.sergigp.quasar.test.dummy.modules.dummyuser.domain.find

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.DummyUser

trait UserFinder {
  def find(id: String): Future[DummyUser]
}
