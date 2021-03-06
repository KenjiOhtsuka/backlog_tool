package com.improvefuture.blt.backlog.domain.backlog.repository

import com.improvefuture.blt.backlog.domain.backlog.factory.IssueFactory
import com.improvefuture.blt.backlog.domain.backlog.model.Issue
import com.nulabinc.backlog4j.api.option.UpdateIssueParams
import com.nulabinc.backlog4j.Issue as BacklogIssue

class IssueRepository(): AbstractBacklogRepository() {
    fun findOne(spaceKey: String, apiKey: String, id: Long): Issue {
        val backlogGateway = buildBacklogClient(spaceKey, apiKey)
        return IssueFactory.createFromBacklogIssue(
                spaceKey, backlogGateway.getIssue(id))
    }

    fun update(
            spaceKey: String, apiKey: String,
            issueParam: Map<String, Any?>): Issue {
        val param = UpdateIssueParams(issueParam["id"] as Long)
        if (issueParam.containsKey("status_id"))
            param.status(BacklogIssue.StatusType.valueOf(issueParam["status_id"] as Int))
        if (issueParam.containsKey("actual_hour"))
            param.actualHours((issueParam["actual_hour"] as Number).toFloat())
        if (issueParam.containsKey("estimated_hour"))
            param.estimatedHours((issueParam["estimated_hour"] as Number).toFloat())
        val backlogIssue = buildBacklogClient(spaceKey, apiKey).updateIssue(param)
        return IssueFactory.createFromBacklogIssue(
                spaceKey, backlogIssue)
    }
}