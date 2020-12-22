package com.improvefuture.blt.backlog.domain.backlog.model

import com.nulabinc.backlog4j.User as BacklogUser

class User: AbstractBacklog() {
    var id: Long? = null
    var key: String? = null
    var name: String? = null
    var emailAddress: String? = null
    var role: BacklogUser.RoleType? = null
    var apiKey: String? = null

    var userId: String = ""
    val urlString: String by lazy {
        "https://$spaceKey.backlog.jp/user/$key"
    }
}