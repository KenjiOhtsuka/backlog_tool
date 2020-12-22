package com.improvefuture.blt.backlog.domain.backlog.service

import com.improvefuture.blt.backlog.domain.backlog.model.Issue
import com.improvefuture.blt.backlog.domain.backlog.model.IssueType
import com.improvefuture.blt.backlog.domain.backlog.repository.BacklogRepository
import com.improvefuture.blt.backlog.domain.backlog.repository.IssueRepository
import com.improvefuture.blt.backlog.domain.backlog.repository.IssueTypeRepository

class IssueService {
    lateinit private var issueRepository: IssueRepository

    lateinit private var backlogRepository: BacklogRepository

    fun findIssue(spaceKey: String, apiKey: String, id: Long): Issue {
        return issueRepository.findOne(spaceKey, apiKey, id)
    }

    fun findAllNonParentIssue(
            spaceKey: String, apiKey: String, projectKey: String,
            issueTypeId: Long?, milestoneId: Long, categoryId: Long?): List<Issue> {
        return backlogRepository.findAllIssues(
                spaceKey, apiKey, projectKey, issueTypeId, milestoneId, categoryId)
    }

    fun findAllUnclosedIssue(
            spaceKey: String, apiKey: String, projectKey: String, milestoneId: Long? = null, categoryId: Long? = null): List<Issue> {
        return backlogRepository.findAllUnclosedIssues(
                spaceKey, apiKey, projectKey, milestoneId, categoryId)
    }

    fun findAllIssueForGanttChart(
            spaceKey: String, apiKey: String, projectKey: String): List<Issue> {
        return backlogRepository.findAllIssuesInStartOrder(
                spaceKey, apiKey, projectKey)
    }

    fun update(
            spaceKey: String, apiKey: String,
            id: Long, valueMap: MutableMap<String, Any?>): Issue {
        valueMap["id"] = id
        return issueRepository.update(
                spaceKey, apiKey, valueMap)
    }

    fun findAllIssueType(
            spaceKey: String, apiKey: String, projectKey: String): List<IssueType> {
        return IssueTypeRepository.findAll(spaceKey, apiKey, projectKey)
    }
}