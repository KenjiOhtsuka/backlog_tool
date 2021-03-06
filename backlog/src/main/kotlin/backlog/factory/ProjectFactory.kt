package com.improvefuture.blt.backlog.domain.backlog.factory

import com.improvefuture.blt.backlog.domain.backlog.model.Project
import com.nulabinc.backlog4j.Project as BacklogProject

object ProjectFactory {
    fun createFromBacklogProject(
            spaceKey: String,
            backlogProject: BacklogProject): Project {
        val project = Project()
        project.spaceKey = spaceKey
        project.id = backlogProject.id
        project.name = backlogProject.name
        project.key = backlogProject.projectKey
        project.orderNumber = backlogProject.displayOrder
        return project
    }
}