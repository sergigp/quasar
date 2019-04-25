package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.event.UserCreatedDomainEvent
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.send.EmailSender

object EventHandlers {
  val dummyHandler: UserCreatedDomainEvent => Future[Unit] =
    (_: UserCreatedDomainEvent) => Future.successful(())

  def handlerWithService(emailSender: EmailSender): UserCreatedDomainEvent => Future[Unit] =
    (e: UserCreatedDomainEvent) => emailSender.send(e.email)
}
