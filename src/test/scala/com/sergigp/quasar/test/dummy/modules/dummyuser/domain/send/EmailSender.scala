package com.sergigp.quasar.test.dummy.modules.dummyuser.domain.send

import scala.concurrent.Future

trait EmailSender {
  def send(email: String): Future[Unit]
}
