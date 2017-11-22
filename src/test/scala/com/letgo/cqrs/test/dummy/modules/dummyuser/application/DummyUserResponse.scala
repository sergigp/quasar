package com.letgo.cqrs.test.dummy.modules.dummyuser.application

import com.letgo.cqrs.query.QueryResponse

case class DummyUserResponse(id: String, name: String) extends QueryResponse

