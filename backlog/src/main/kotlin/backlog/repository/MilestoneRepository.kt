package com.improvefuture.blt.backlog.domain.backlog.repository

import com.improvefuture.blt.backlog.domain.backlog.factory.MilestoneFactory
import com.improvefuture.blt.backlog.domain.backlog.model.Milestone

class MilestoneRepository: AbstractBacklogRepository() {
    fun findAll(spaceKey: String, apiKey: String, projectKey: String): List<Milestone> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getMilestones(projectKey).map {
            MilestoneFactory.createFromBacklogMilestone(spaceKey, it)
        }
    }
}