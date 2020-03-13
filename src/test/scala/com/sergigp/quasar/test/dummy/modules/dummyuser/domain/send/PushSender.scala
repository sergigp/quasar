package com.sergigp.quasar.test.dummy.modules.dummyuser.domain.send

import scala.concurrent.Future

trait PushSender {
  def send(id: String): Future[Unit]
}
