package com.improvefuture.blt.backlog.domain.backlog.repository

import com.nulabinc.backlog4j.BacklogClient
import com.nulabinc.backlog4j.BacklogClientFactory
import com.nulabinc.backlog4j.conf.BacklogComConfigure
import com.nulabinc.backlog4j.conf.BacklogConfigure
import com.nulabinc.backlog4j.conf.BacklogJpConfigure
import com.nulabinc.backlog4j.conf.BacklogToolConfigure

abstract class AbstractBacklogRepository {
//    protected fun buildBacklogClient(
//            spaceKey: String, apiKey: String): BacklogClient {
//        val configure: BacklogConfigure =
//                BacklogJpConfigure(spaceKey).apiKey(apiKey)
//        return BacklogClientFactory(configure).newClient()
//    }
    protected fun buildBacklogClient(domain: String, apiKey: String): BacklogClient {
        val domainChunks = domain.split('.')
        val identifier = domainChunks.takeLast(2).joinToString(".")
        val spaceId = domainChunks[0]
        val configure = when (identifier) {
            "backlog.jp" -> BacklogJpConfigure(spaceId)
            "backlog.com" -> BacklogComConfigure(spaceId)
            "backlogtool.com" -> BacklogToolConfigure(spaceId)
            else -> BacklogJpConfigure(spaceId)
        }
        configure.apiKey(apiKey)
        return BacklogClientFactory(configure).newClient()
    }
}