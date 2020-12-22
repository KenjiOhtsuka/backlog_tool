package com.improvefuture.blt.backlog.domain.backlog.master

import com.nulabinc.backlog4j.Issue

enum class IssuePriority(val backlogPriorityType: Issue.PriorityType) {
    Low(Issue.PriorityType.Low),
    Normal(Issue.PriorityType.Normal),
    High(Issue.PriorityType.High);
}
