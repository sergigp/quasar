package com.sergigp.quasar.test.dummy.modules.dummyuser.application.event

import com.sergigp.quasar.event.Event

case class UserCreatedDomainEvent(id: String, name: String, email: String) extends Event {
  override type EventReturnType = Unit
}
