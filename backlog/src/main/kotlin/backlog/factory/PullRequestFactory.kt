package backlog.factory

import com.improvefuture.blt.backlog.domain.backlog.factory.UserFactory
import com.improvefuture.blt.backlog.domain.backlog.model.PullRequest
import com.nulabinc.backlog4j.PullRequest as BacklogPullRequest

object PullRequestFactory {
    fun createFromBacklogPullRequest(
        spaceKey: String, backlogPullRequest: BacklogPullRequest
    ): PullRequest {
        val pullRequest = PullRequest()
        pullRequest.number = backlogPullRequest.number
        backlogPullRequest.assignee?.let {
            pullRequest.assignee = UserFactory.createFromBacklogUser(
                spaceKey, it
            )
        }
        pullRequest.title = backlogPullRequest.summary
        pullRequest.description = backlogPullRequest.description
        pullRequest.createdAt = backlogPullRequest.created
        backlogPullRequest.createdUser?.let {
            pullRequest.createdBy = UserFactory.createFromBacklogUser(
                spaceKey, it
            )
        }
        pullRequest.updatedAt = backlogPullRequest.updated
        backlogPullRequest.updatedUser?.let {
            pullRequest.updatedBy = UserFactory.createFromBacklogUser(
                spaceKey, it
            )
        }

        pullRequest.branch = backlogPullRequest.branch
        pullRequest.destinationBranch = backlogPullRequest.base
        return pullRequest
    }
}