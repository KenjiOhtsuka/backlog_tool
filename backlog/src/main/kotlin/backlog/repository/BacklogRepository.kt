package com.improvefuture.blt.backlog.domain.backlog.repository

import backlog.factory.PullRequestFactory
import com.improvefuture.blt.backlog.domain.backlog.factory.IssueFactory
import com.improvefuture.blt.backlog.domain.backlog.factory.ProjectFactory
import com.improvefuture.blt.backlog.domain.backlog.model.Issue
import com.improvefuture.blt.backlog.domain.backlog.model.Project
import com.improvefuture.blt.backlog.domain.backlog.model.PullRequest
import com.improvefuture.blt.backlog.gateway.WebGateway
import com.nulabinc.backlog4j.IssueType
import com.nulabinc.backlog4j.CustomField as BacklogCustomField
import com.nulabinc.backlog4j.Issue as BacklogIssue
import com.nulabinc.backlog4j.Project as BacklogProject
import com.nulabinc.backlog4j.PullRequest as BacklogPullRequest
import com.nulabinc.backlog4j.api.option.GetIssuesParams
import com.nulabinc.backlog4j.api.option.PullRequestQueryParams
import com.nulabinc.backlog4j.api.option.UpdateIssueParams
import java.net.URL

class BacklogRepository(): AbstractBacklogRepository() {
    fun findAllProjects(spaceKey: String, apiKey: String): List<Project> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.projects.map {
            ProjectFactory.createFromBacklogProject(
                    spaceKey, it) }
    }

    fun findProject(spaceKey: String, apiKey: String, key: String): Project {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return ProjectFactory.createFromBacklogProject(
                spaceKey,
                backlogGateway.getProject(key))
    }

    fun findAllPullRequests(spaceKey: String, apiKey: String, key: String, repository: String): List<PullRequest> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getPullRequests(key, repository).map {
            PullRequestFactory.createFromBacklogPullRequest(
                spaceKey, it
            )
        }
    }

    fun findAllOpenPullRequests(
        spaceKey: String, apiKey: String, key: String, repository: String
    ): List<PullRequest> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        val param = PullRequestQueryParams()
        param.statusType(listOf(com.nulabinc.backlog4j.PullRequest.StatusType.Open))
        param.count(100)
        return backlogGateway.getPullRequests(key, repository, param).map {
            PullRequestFactory.createFromBacklogPullRequest(
                spaceKey, it
            )
        }
    }

    fun findPullRequests(
        spaceKey: String, apiKey: String, key: String, repository: String,
        statusList: List<com.nulabinc.backlog4j.PullRequest.StatusType> = listOf()
    ): List<PullRequest> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        val param = PullRequestQueryParams()
        if (statusList.isNotEmpty()) { param.statusType(statusList) }
        param.count(100)
        return backlogGateway.getPullRequests(key, repository, param).map {
            PullRequestFactory.createFromBacklogPullRequest(
                spaceKey, it
            )
        }
    }

    fun findPullRequest(
        spaceKey: String, apiKey: String, key: String, repository: String,
        id: Long
    ): PullRequest? {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getPullRequest(key, repository, id)?.let {
            PullRequestFactory.createFromBacklogPullRequest(
                spaceKey, it
            )
        }
    }

    fun findIssues(
        spaceKey: String, apiKey: String, key: String,
        statusList: List<com.nulabinc.backlog4j.Issue.StatusType> = listOf()
    ): List<Issue> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        val project = findProject(spaceKey, apiKey, key)
        val param = GetIssuesParams(listOf(project.id))
        param.count(100)
        if (statusList.isEmpty()) {
            param.statuses(listOf(
                com.nulabinc.backlog4j.Issue.StatusType.Open,
                com.nulabinc.backlog4j.Issue.StatusType.InProgress,
                com.nulabinc.backlog4j.Issue.StatusType.Resolved,
                com.nulabinc.backlog4j.Issue.StatusType.Custom
            ))
        }

        return backlogGateway.getIssues(param).map {
            IssueFactory.createFromBacklogIssue(
                spaceKey, it
            )
        }
    }

    fun findIssue(
        spaceKey: String, apiKey: String, key: String,
        id: Long
    ): Issue? {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)

        return backlogGateway.getIssue(id)?.let {
            IssueFactory.createFromBacklogIssue(
                spaceKey, it
            )
        }
    }

    fun findIssue(
        spaceKey: String, apiKey: String, key: String
    ): Issue? {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)

        return backlogGateway.getIssue(key)?.let {
            IssueFactory.createFromBacklogIssue(
                spaceKey, it
            )
        }
    }

    /**
     * @param projectKey
     * Return all un-closed issues
     */
    fun findAllUnclosedIssues(
            spaceKey: String, apiKey: String,
            projectKey: String, milestoneId: Long?, categoryId: Long?): List<Issue> {
        val project = findProject(spaceKey, apiKey, projectKey)
        val issueParam = GetIssueParam(listOf(project.id!!))
        if (milestoneId != null) issueParam.milestoneIds(listOf(milestoneId))
        if (categoryId != null) issueParam.categoryIds(listOf(categoryId))
        issueParam.statuses(
                listOf(
                        BacklogIssue.StatusType.Open,
                        BacklogIssue.StatusType.InProgress,
                        BacklogIssue.StatusType.Resolved))
        issueParam.sort(GetIssuesParams.SortKey.DueDate)
        issueParam.order(GetIssuesParams.Order.Asc)
        issueParam.count(100)

        return findAllIssueMap(
                spaceKey, apiKey, issueParam).values.toList()
    }

    fun findAllIssues(
            spaceKey: String, apiKey: String,
            projectKey: String, issueTypeId: Long?, milestoneId: Long, categoryId: Long?): List<Issue> {
        val project = findProject(spaceKey, apiKey, projectKey)

        fun buildCommonIssueParam(): GetIssueParam {
            var issueParam = GetIssueParam(listOf(project.id!!))
            if (issueTypeId != null) issueParam.issueTypeIds(listOf(issueTypeId))
            issueParam.milestoneIds(listOf(milestoneId))
            if (categoryId != null) issueParam.categoryIds(listOf(categoryId))
            issueParam.resolutions(listOf(
                    BacklogIssue.ResolutionType.NotSet,
                    BacklogIssue.ResolutionType.CannotReproduce,
                    BacklogIssue.ResolutionType.Fixed))
            issueParam.sort(GetIssuesParams.SortKey.DueDate)
            issueParam.order(GetIssuesParams.Order.Asc)
            issueParam.count(100)
            return issueParam
        }

        var issueParam = buildCommonIssueParam()
        issueParam.parentChildType(GetIssuesParams.ParentChildType.Child)

        var issueList = findAllIssueList(
                spaceKey, apiKey, issueParam)

        issueParam = buildCommonIssueParam()
        issueParam.parentChildType(GetIssuesParams.ParentChildType.NotChildNotParent)

        issueList += findAllIssueList(
                spaceKey, apiKey, issueParam)

        return issueList
    }

    fun findAllIssuesInStartOrder(spaceKey: String, apiKey: String, projectKey: String): List<Issue> {
        val project = findProject(spaceKey, apiKey, projectKey)
        val issueParam = GetIssueParam(listOf(project.id!!))
        issueParam.statuses(
                listOf(
                        BacklogIssue.StatusType.Open,
                        BacklogIssue.StatusType.InProgress,
                        BacklogIssue.StatusType.Resolved))
        issueParam.sort(GetIssuesParams.SortKey.StartDate)
        issueParam.order(GetIssuesParams.Order.Asc)
        issueParam.sort(GetIssuesParams.SortKey.DueDate)
        issueParam.order(GetIssuesParams.Order.Asc)
        issueParam.count(100)

        return findAllIssueMap(
                spaceKey, apiKey, issueParam).values.toList()
    }

    fun retrieveUserIcon(
            spaceKey: String, apiKey: String, userId: Long): ByteArray {
        return WebGateway.getImage(
                buildUserIconUrl(spaceKey, apiKey, userId),
                mapOf(
                        "apiKey" to apiKey
                ))
    }

    fun retrieveProjectIcon(
            spaceKey: String, apiKey: String, projectKey: String): ByteArray {
        return WebGateway.getImage(
                buildProjectIconUrl(spaceKey, apiKey, projectKey),
                mapOf("apiKey" to apiKey)
        )
    }

    private fun buildUserIconUrl(
            spaceKey: String, apiKey: String, userId: Long): URL {
        return URL("https://$spaceKey.backlog.jp/api/v2/users/$userId/icon?apiKey=$apiKey")
    }

    private fun buildProjectIconUrl(
            spaceKey: String, apiKey: String, projectKey: String): URL {
        return URL( "https://$spaceKey.backlog.jp/api/v2/projects/$projectKey/image?apiKey=$apiKey")
    }

    private fun findAllIssueMap(
            spaceKey: String, apiKey: String, condition: GetIssueParam): Map<Long, Issue> {
        val issueList = findAllIssueList(
                spaceKey, apiKey, condition)
        val parentIssuesMap = mutableMapOf<Long, Issue>()
        issueList.forEach {
            if (!it.isChild())
                parentIssuesMap.put(it.id!!, it)
        }
        issueList.forEach {
            if (it.isChild())
                parentIssuesMap[it.parentIssueId!!]?.addChild(it) ?:
                        parentIssuesMap.put(it.id!!, it)
        }
        return parentIssuesMap
    }

    private fun findAllIssueList(
            spaceKey: String, apiKey: String, condition: GetIssueParam): List<Issue> {
        val issueList = mutableListOf<Issue>()
        do {
            val param = condition.clone()
            param.offset(issueList.count().toLong())
            val issueChunks = this.findIssues(
                    spaceKey, apiKey, param)
            issueList += issueChunks
        } while (issueChunks.count() > 0)
        return issueList
    }

    private fun buildClosedIssueCondition(projectId: Long):
            GetIssuesParams {
        val issueParam: GetIssuesParams =
                GetIssuesParams(mutableListOf(projectId))
        issueParam.statuses(listOf(BacklogIssue.StatusType.Closed))
        issueParam.resolutions(listOf(
                BacklogIssue.ResolutionType.CannotReproduce,
                BacklogIssue.ResolutionType.Fixed,
                BacklogIssue.ResolutionType.NotSet,
                BacklogIssue.ResolutionType.WontFix))
        return issueParam
    }

    private fun buildFinishedPrCondition(): PullRequestQueryParams {
        var prParam = PullRequestQueryParams()
        prParam.statusType(listOf(
                BacklogPullRequest.StatusType.Closed,
                BacklogPullRequest.StatusType.Merged))
        return prParam
    }

    /**
     * Find and return Issue List
     */
    private fun findIssues(
            spaceKey: String, apiKey: String, condition: GetIssueParam): List<Issue> {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return backlogGateway.getIssues(condition).
                map { IssueFactory.createFromBacklogIssue(spaceKey, it) }
    }
}