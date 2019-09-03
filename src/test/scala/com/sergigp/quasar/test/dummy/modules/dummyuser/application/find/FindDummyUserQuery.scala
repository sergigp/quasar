package com.sergigp.quasar.test.dummy.modules.dummyuser.application.find

import com.sergigp.quasar.query.Query
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserResponse
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.FindDummyUserError.FindDummyUserError

case class FindDummyUserQuery(id: String) extends Query {
  override type QueryResponse = Either[FindDummyUserError, DummyUserResponse]
}
