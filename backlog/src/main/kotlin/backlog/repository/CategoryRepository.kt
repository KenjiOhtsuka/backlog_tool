package com.improvefuture.blt.backlog.domain.backlog.repository

import com.improvefuture.blt.backlog.domain.backlog.factory.CategoryFactory
import com.improvefuture.blt.backlog.domain.backlog.model.Category

class CategoryRepository: AbstractBacklogRepository() {
    fun findAllCategory(
            spaceKey: String, apiKey: String, projectKey: String): List<Category> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getCategories(projectKey).map {
            CategoryFactory.createFromBacklogCategory(
                    spaceKey, it)
        }
    }
}