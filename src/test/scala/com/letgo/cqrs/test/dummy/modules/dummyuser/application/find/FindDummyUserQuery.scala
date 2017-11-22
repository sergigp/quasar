package com.letgo.cqrs.test.dummy.modules.dummyuser.application.find

import com.letgo.cqrs.query.Query
import com.letgo.cqrs.test.dummy.modules.dummyuser.application.DummyUserResponse
import FindDummyUserError.FindDummyUserError

case class FindDummyUserQuery(id: String) extends Query {
  override type QueryError = FindDummyUserError
  override type QueryResponse = DummyUserResponse
}
