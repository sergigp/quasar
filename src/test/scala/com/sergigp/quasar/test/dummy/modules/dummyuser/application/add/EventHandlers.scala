package com.sergigp.quasar.test.dummy.modules.dummyuser.application.add

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.event.UserCreatedDomainEvent
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.send.{EmailSender, PushSender}

object EventHandlers {
  val dummyHandler: UserCreatedDomainEvent => Future[Unit] =
    (_: UserCreatedDomainEvent) => Future.successful(())

  def handlerWithEmailSender(emailSender: EmailSender): UserCreatedDomainEvent => Future[Unit] =
    (e: UserCreatedDomainEvent) => emailSender.send(e.email)

  def handlerWithPushSender(pushSender: PushSender): UserCreatedDomainEvent => Future[Unit] =
    (e: UserCreatedDomainEvent) => pushSender.send(e.id)
}
