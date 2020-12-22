package com.improvefuture.blt.backlog.domain.backlog.service

import com.improvefuture.blt.backlog.domain.backlog.model.*
import com.improvefuture.blt.backlog.domain.backlog.repository.BacklogRepository
import com.improvefuture.blt.backlog.domain.backlog.repository.CategoryRepository
import com.improvefuture.blt.backlog.domain.backlog.repository.MilestoneRepository


class BacklogService() {
    private val backlogRepository = BacklogRepository()

    private val milestoneRepository = MilestoneRepository()

    private val categoryRepository = CategoryRepository()

//    fun findAllIssue(spaceKey: String, apiKey: String, projectKey: String): List<Issue> {
//        return backlogRepository.findAllIssues(
//                spaceKey, apiKey, projectKey)
//    }

    fun findAllProject(spaceKey: String, apiKey: String): List<Project> {
        return backlogRepository.findAllProjects(spaceKey, apiKey)
    }

    fun findAllPullRequest(spaceKey: String, apiKey: String, projectKey: String, repository: String): List<PullRequest> {
        return backlogRepository.findAllPullRequests(spaceKey, apiKey, projectKey, repository)
    }

    fun findAllOpenPullRequest(spaceKey: String, apiKey: String, projectKey: String, repository: String): List<PullRequest> {
        return backlogRepository.findPullRequests(
            spaceKey, apiKey, projectKey, repository, listOf(com.nulabinc.backlog4j.PullRequest.StatusType.Open))
    }

    fun findPullRequest(spaceKey: String, apiKey: String, projectKey: String, repository: String, id: Long): PullRequest? {
        return backlogRepository.findPullRequest(
            spaceKey, apiKey, projectKey, repository, id)
    }

    fun findIssues(spaceKey: String, apiKey: String, projectKey: String): List<Issue> {
        return backlogRepository.findIssues(spaceKey, apiKey, projectKey)
    }

    fun findIssue(spaceKey: String, apiKey: String, projectKey: String, id: Long): Issue? {
        return backlogRepository.findIssue(spaceKey, apiKey, "${projectKey}-${id}")
    }

    fun findIssue(spaceKey: String, apiKey: String, projectKey: String, key: String): Issue? {
        val (projectKey, id) = key.split("-")
        return backlogRepository.findIssue(spaceKey, apiKey, key)
    }

    fun retrieveUserIcon(
            spaceKey: String, apiKey: String, userId: Long): ByteArray {
        return backlogRepository.retrieveUserIcon(spaceKey, apiKey, userId)
    }

    fun retrieveProjectIcon(
            spaceKey: String, apiKey: String, projectKey: String): ByteArray {
        return backlogRepository.retrieveProjectIcon(
                spaceKey, apiKey, projectKey)
    }

    fun findAllMilestone(
            spaceKey: String, apiKey: String, projectKey: String): List<Milestone> {
        return milestoneRepository.findAll(
                spaceKey, apiKey, projectKey)
    }

    fun findAllCategory(spaceKey: String, apiKey: String, projectKey: String): List<Category> {
        return categoryRepository.findAllCategory(
                spaceKey, apiKey, projectKey)
    }
}
