package com.improvefuture.blt.backlog.domain.backlog.model

import java.util.*

class PullRequest {
    var number: Long? = null
    var title: String? = null
    var description: String? = null
    var assignee: User? = null

    var creator: User? = null
    var createdAt: Date? = null
    var createdBy: User? = null
    var updatedAt: Date? = null
    var updatedBy: User? = null

    var branch: String? = null
    var destinationBranch: String? = null
}