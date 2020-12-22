package com.improvefuture.blt.backlog.domain.backlog.factory

import com.improvefuture.blt.backlog.domain.backlog.model.User
import com.nulabinc.backlog4j.User as BacklogUser

object UserFactory {
    fun createFromBacklogUser(
            spaceKey: String, backlogUser: BacklogUser, apiKey: String? = null): User {
        val user = User()
        user.id = backlogUser.id
        user.key = backlogUser.userId
        user.name = backlogUser.name
        user.emailAddress = backlogUser.mailAddress
        user.role = backlogUser.roleType
        user.spaceKey = spaceKey
        apiKey?.let { user.apiKey = it }
        return user
    }
}