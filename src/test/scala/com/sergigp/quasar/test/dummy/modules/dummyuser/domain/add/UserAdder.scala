package com.sergigp.quasar.test.dummy.modules.dummyuser.domain.add

import scala.concurrent.Future

trait UserAdder {
  def add(id: String, name: String): Future[Unit]
}
