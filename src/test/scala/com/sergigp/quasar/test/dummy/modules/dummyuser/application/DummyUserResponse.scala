package com.sergigp.quasar.test.dummy.modules.dummyuser.application

import com.sergigp.quasar.query.QueryResponse

case class DummyUserResponse(id: String, name: String, email: String) extends QueryResponse
