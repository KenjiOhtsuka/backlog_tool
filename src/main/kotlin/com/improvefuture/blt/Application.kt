package com.improvefuture.blt

import com.improvefuture.blt.backlog.domain.backlog.model.Issue
import com.improvefuture.blt.backlog.domain.backlog.repository.BacklogRepository
import com.improvefuture.blt.backlog.domain.backlog.service.BacklogService
import com.improvefuture.blt.config.Config
import com.improvefuture.blt.presentation.BacklogController
import kotlinx.cli.*
import java.io.File
import java.io.FileInputStream
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.Format

object Application {
    @JvmStatic
    fun main(vararg args: String) {
//            if (java.io.File("config").exists())
//                File("config").createNewFile()
//            FileInputStream("config").bufferedReader().use {
//                it.readLine()
//            }
        var config: Config
        while (true) {
            val temporalConfig = setUpConfig()
            if (temporalConfig != null) {
                config = temporalConfig
                break
            }
        }
        println("config: ${config.domain}")

        var project: String? = null
        var repository: String? = null

        while (true) {
            print("> ")
            val command = readLine()
            if (command.isNullOrBlank()) continue
            val args = command.split(" ").filter { !it.isNullOrBlank() }

            when (args[0].toLowerCase()) {
                "set" -> {
                    when (args[1]) {
                        "project", "proj" -> {
                            project = args[2].toUpperCase()
                            println("The project is set to ${project}.")
                        }
                        "repository", "repo" -> {
                            repository = args[2]
                            println("The repository is set to ${repository}.")
                        }
                    }
                }
                "config" -> {
                    if (args.size == 1) {
                        // show config
                        println("Current config:")
                        println("  domain: ${config.domain}")
                        println("  apiKey: ${config.apiKey}")
                        continue
                    }
                    when (args[1]) {
                        "list" -> {

                        }
                    }
                }
                "pr", "pullrequest" -> {
                    if (project.isNullOrBlank()) {
                        println("Please set project (set project XXXXX)")
                        continue
                    }
                    if (repository.isNullOrBlank()) {
                        println("Please set repository (set repo XXXXX)")
                        continue
                    }

                    if (args.size == 1) {
                        val prs = BacklogService().findAllOpenPullRequest(
                            config.domain, config.apiKey, project, repository
                        )
                        prs.forEach {
                            println("${it.number} ${it.title}")
                        }
                        continue
                    }
                    when (args[1]) {
                        "open", "new", "create" -> {

                        }
                        "close" -> {

                        }
                    }
                    if (Regex("\\d+").matches(args[1])) {
                        if (args.size == 2) {
                            // show
                            val pullRequest = BacklogService().findPullRequest(
                                config.domain, config.apiKey, project,
                                repository, args[1].toLong()
                            )
                            if (pullRequest == null) {
                                println("PR wasn't found.")
                                continue
                            }
                            BacklogController.showPullRequest(pullRequest)

                            // show comments
                        }
                        continue
                    }
                    when (args[2]) {

                    }
                }
                "issue" -> {
                    if (project.isNullOrBlank()) {
                        println("Please set project (set project XXXXX)")
                        continue
                    }
                    if (args.size == 1) {
                        // list issues
                        BacklogController.listIssues(
                            BacklogService().findIssues(
                                config.domain, config.apiKey, project
                            )
                        )
                        continue
                    }
                    when (args[1]) {
                        "create", "new" -> {

                        }
                        "delete" -> {

                        }
                    }
                    if (args.size == 2) {

                    }
                    val key =
                        if (isNumeric(args[1])) "${project}-${args[1]}" else args[1]
                    val issue = BacklogService().findIssue(
                        config.spaceId, config.apiKey, project, key
                    )

                    if (issue == null) {
                        println("Issue wasn't found.")
                        continue
                    }
                    BacklogController.showIssue(issue)
                    // list comment (created desc)
                    continue
                }
                "milestone", "ms" -> {
                    if (args.size == 1) {
                        // list
                        continue
                    }
                    when (args[1]) {
                        "create", "new" -> {

                        }
                        "delete", "remove" -> {

                        }
                        "hide" -> {

                        }
                    }
                    if (isNumeric(args[1])) {

                    }
                }
                "repository", "repo" -> {
                    if (args.size == 1) {
                        // list
                        continue
                    }
                }
            }
        }
    }

    fun parse(vararg args: String) {
//            val parser = ArgParser("example")
//            val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
//            val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")
//            val format by parser.option(ArgType.Choice<Format>(), shortName = "f",
//                description = "Format for output file").default(Format.CSV).multiple()
//            val stringFormat by parser.option(ArgType.Choice(listOf("html", "csv", "pdf"), { it }), shortName = "sf",
//                description = "Format as string for output file").default("csv").multiple()
//            val debug by parser.option(ArgType.Boolean, shortName = "d", description = "Turn on debug mode").default(false)
//            val eps by parser.option(ArgType.Double, description = "Observational error").default(0.01)
//
//            parser.parse(args)
//            val inputData = readFrom(input)
//            val result = calculate(inputData, eps, debug)
//            format.forEach {
//                produce(result, it, output)
//            }
    }

    private fun setUpConfig(): Config? {
        val configMap = loadConfig()
        if (configMap.isEmpty()) {
            print("input domain: ")
            val domain = readLine()?.toLowerCase()
            print("input api key: ")
            val apiKey = readLine()
            while (true) {
                print("Save? [y/N]:")
                val judge = readLine()
                if (judge.isNullOrBlank()) continue
                if (judge[0].toUpperCase() == 'Y') {
                    // save
                    return saveConfig(domain!!, apiKey!!)
                    break
                }
                if (judge[0].toUpperCase() == 'N') {
                    // don't save
                    return null
                }
            }
        }
        if (configMap.size > 2) {
            println("Choose config:")
            val configKeyList = configMap.keys.toList()
            configKeyList.forEachIndexed { i, k ->
                println("  ${i}: ${k}")
            }
            while (true) {
                print("> ")
                val numberString = readLine()
                if (numberString.isNullOrBlank()) continue
                if (!isNumeric(numberString)) {
                    print("Please input number.")
                    break
                }
                val number = numberString.toInt()
                if (0 <= number && number < configKeyList.size) {
                    val domain = configKeyList[number]
                    return configMap[domain]
                }
                print("Out of range.")
            }
        }
        return configMap[configMap.keys.first()]
    }

    private fun isNumeric(text: String): Boolean {
        return Regex("\\d+").matches(text)
    }

    private fun saveConfig(key: String, apiKey: String): Config {
        val configMap = loadConfig()
        configMap[key] = Config(key, apiKey)
        val text = Json.encodeToString(mapOf(key to Config(key, apiKey)))
        val path = getConfigPath()
        if (!Files.exists(path)) {
            Files.createDirectories(path.parent)
            Files.createFile(path)
        }

        Files.writeString(path, text)
        return configMap[key]!!
    }

    private fun loadConfig(): MutableMap<String, Config> {
        val path = getConfigPath()
        if (Files.exists(path)) {
            val text = Files.readString(path)
            return Json.decodeFromString<MutableMap<String, Config>>(text)
        }
        return mutableMapOf()
    }

    private fun getConfigPath(): Path {
        val home = System.getProperty("user.home")
        return Paths.get(home, ".blt", "config")
    }
}