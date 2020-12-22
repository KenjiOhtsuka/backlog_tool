package com.improvefuture.blt.backlog.domain.backlog.model

class IssueType: AbstractBacklog() {
    var id: Long? = null
    var color: IssueTypeColor? = null
    var name: String? = null
}