package com.sergigp.quasar.test.dummy.modules.dummyuser.application.find

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserResponse
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.FindDummyUserError.FindDummyUserError

import com.sergigp.quasar.query.Query

case class FindDummyUserQuery(id: String) extends Query {
  override type QueryError = FindDummyUserError
  override type QueryResponse = DummyUserResponse
}
