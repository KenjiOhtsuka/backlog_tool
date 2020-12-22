package com.improvefuture.blt.backlog.domain.backlog.repository

import com.improvefuture.blt.backlog.domain.backlog.factory.IssueTypeFactory
import com.improvefuture.blt.backlog.domain.backlog.model.IssueType

object IssueTypeRepository: AbstractBacklogRepository() {
    fun findAll(spaceKey: String, apiKey: String, projectKey: String): List<IssueType> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getIssueTypes(projectKey).
                map { IssueTypeFactory.createFromBacklogIssueType(
                        spaceKey, it) }
    }
}