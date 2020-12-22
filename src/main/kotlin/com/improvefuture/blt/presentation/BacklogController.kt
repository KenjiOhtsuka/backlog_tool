package com.improvefuture.blt.presentation

import com.improvefuture.blt.backlog.domain.backlog.model.Issue
import com.improvefuture.blt.backlog.domain.backlog.model.PullRequest

object BacklogController {
    fun showIssue(issue: Issue) {
        if (issue.title.isNullOrBlank()) println("(no title)")
        else println(issue.title)
        println()
        if (issue.detail.isNullOrBlank()) println("(no detail)")
        else println(issue.detail)
        println()
        println("Creator: " + (issue.creator?.let { "${it.name} (${it.emailAddress})" } ))
        println("Assignee: " + (issue.assignee?.let { "${it.name} (${it.emailAddress})" } ?: "(none)"))
        println("Due Date: " + (issue.dueDate ?: "(none)"))
    }

    fun listIssues(issues: Collection<Issue>) {
        issues.forEach { println("${it.key} ${it.title}") }
    }
    
    fun showPullRequest(pullRequest: PullRequest) {
        if (pullRequest.title.isNullOrBlank()) println("(no title)")
        else println(pullRequest.title)
        println()
        if (pullRequest.description.isNullOrBlank()) println("(no description)")
        else println(pullRequest.description)
        println()
        println("${pullRequest.branch} -> ${pullRequest.destinationBranch}")
    }

    fun listPullRequest(pullRequests: Collection<PullRequest>) {

    }
}