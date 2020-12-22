package com.improvefuture.blt.backlog.domain.backlog.factory

import com.improvefuture.blt.backlog.domain.backlog.model.Category
import com.nulabinc.backlog4j.Category as BacklogCategory

object CategoryFactory {
    fun createFromBacklogCategory(spaceKey: String, backlogCategory: BacklogCategory): Category {
        val category = Category()
        category.id = backlogCategory.id
        category.name = backlogCategory.name
        return category
    }
}