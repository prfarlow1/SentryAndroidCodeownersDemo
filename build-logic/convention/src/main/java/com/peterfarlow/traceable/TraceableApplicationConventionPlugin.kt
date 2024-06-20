package com.peterfarlow.traceable

import com.peterfarlow.KtorClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register

class TraceableApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register<TopLevelPostSentryMappingsTask>("createAllSentryMappings") {
            group = "Convention"
            description = "send codemappings for all traceable library modules to Sentry API"
        }
    }
}

abstract class TopLevelPostSentryMappingsTask : DefaultTask() {

    private var slug: String = ""

    @Option(option = "slug", description = "slug")
    fun setSlug(slug: String) {
        this.slug = slug
    }

    private var token: String = ""

    @Option(option = "token", description = "token")
    fun setToken(token: String) {
        this.token = token
    }

    private var integrationId: String = ""

    @Option(option = "integrationId", description = "integrationId")
    fun setIntegrationId(integrationId: String) {
        this.integrationId = integrationId
    }

    private var defaultBranch: String = "main"

    @Option(option = "defaultBranch", description = "defaultBranch")
    fun setDefaultBranch(defaultBranch: String) {
        this.defaultBranch = defaultBranch
    }

    private var projectId: String = ""

    @Option(option = "projectId", description = "projectId")
    fun setProjectId(projectId: String) {
        this.projectId = projectId
    }

    private var repositoryId: String = ""

    @Option(option = "repositoryId", description = "repositoryId")
    fun setRepositoryId(repositoryId: String) {
        this.repositoryId = repositoryId
    }

    private var dry: Boolean = false

    @Option(option = "dry", description = "dry")
    fun setDry(dry: Boolean) {
        this.dry = dry
    }

    @TaskAction
    fun postRequest() {
        val root = project.rootProject
        val excludes = setOf("app") + root.name
        root.allprojects
            .filter { it.name !in excludes }
            .forEach { project ->
                makeRequest(project)
            }
    }

    private fun makeRequest(project: Project) {
        val body = getBody(project)
        logger.info(body.toString())
        logger.info("About to send mapping for ${project.name}\nsourceRoot: ${body.sourceRoot}\nstackRoot: ${body.stackRoot}\n---")
        if (dry) {
            return
        }
        try {
            runBlocking {
                try {
                    KtorClientProvider.client.patch("https://us.sentry.io/api/0/organizations/$slug/code-mappings/") {
                        headers {
                            set("Authorization", "Bearer $token")
                        }
                        contentType(ContentType.Application.Json)
                        setBody(body.asRequest())
                    }.also {
                        logger.info(it.body<String>())
                    }
                } catch (e: Exception) {
                    coroutineContext.ensureActive()
                    logger.log(LogLevel.INFO, "failed to send stack trace", e)
                }
            }
        } catch (e: Exception) {
            logger.log(LogLevel.INFO, "failed to send stack trace outer", e)
        }
    }

    private fun getBody(project: Project) = SentryBody(
        defaultBranch = defaultBranch,
        integrationId = integrationId,
        projectId = projectId,
        repositoryId = repositoryId,
        sourceRoot = getSourceRoot(project),
        stackRoot = getStackRoot(project)
    )

    private fun SentryBody.asRequest() = """
    {
        "defaultBranch": "$defaultBranch",
        "stackRoot": "$stackRoot",
        "sourceRoot": "$sourceRoot",
        "repositoryId": "$repositoryId",
        "integrationId": "$integrationId",
        "projectId": "$projectId"
    }
    """.trimIndent()

    private fun getSourceRoot(project: Project): String {
        return "${project.name}/src/main/java/com/peterfarlow/${project.name}"
    }

    private fun getStackRoot(project: Project): String {
        val traceableExtension = project.extensions.findByType<TraceableExtension>()
            .also { logger.debug("project ${project.name} set rootPackageName to ${it?.rootPackageName}") }
        return if (traceableExtension != null) {
            "com/peterfarlow/${traceableExtension.rootPackageName}"
        } else {
            "com/peterfarlow/${project.name}"
        }
    }
}
