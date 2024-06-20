package com.peterfarlow.traceable

import com.peterfarlow.KtorClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.findByType

class TraceableLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("createSentryMapping") {
            group = "Convention"
            description = "send codemapping to Sentry API"
            doLast {
                fun makeRequest(dryRun: Boolean, sourceRoot: String, stackRoot: String) {
                    println("About to send mapping\nsourceRoot: $sourceRoot\nstackRoot: $stackRoot\n---")
                    if (dryRun) return

                    val body = SentryBody(sourceRoot = sourceRoot, stackRoot = stackRoot)
                    runBlocking {
                        KtorClientProvider.client.post("https://us.sentry.io/api/0/organizations/codeowners-demo/code-mappings/") {
                            headers {
                                set("Authorization", "Bearer 7890b09cacf78cdae7805ad8460a6292471cbe20d927fb3623a8d8607b758d45")
                            }
                            contentType(ContentType.Application.Json)
                            setBody(body)
                        }.also {
                            logger.info(it.body<String>())
                        }
                    }
                }
                val root = project.rootProject
                val excludes = setOf("app") + root.name
                root.allprojects
                    .filter { it.name !in excludes }
                    .forEach { project ->
                        makeRequest(
                            true,
                            sourceRoot = "${project.name}/src/main/java/com/peterfarlow",
                            stackRoot = getStackRoot(project),
                        )
                    }
            }
        }
    }

    private fun getStackRoot(project: Project): String {
        val traceableExtension = project.extensions.findByType<TraceableExtension>().also { println("project ${project.name} set rootPackageName to ${it?.rootPackageName}") }
        return if (traceableExtension != null) {
            "com/peterfarlow/${traceableExtension.rootPackageName}"
        } else {
            "com/peterfarlow/${project.name}"
        }
    }
}

abstract class PostSentryMappingsTask : DefaultTask() {

    @Option(option = "slug", description = "slug")
    private var slug: String = ""

    @Option(option = "token", description = "token")
    private var token: String = ""

    @Option(option = "integrationId", description = "integrationId")
    private var integrationId: String = ""

    @Option(option = "defaultBranch", description = "defaultBranch")
    private var defaultBranch: String = "main"

    @Option(option = "projectId", description = "projectId")
    private var projectId: String = ""

    @Option(option = "repositoryId", description = "repositoryId")
    private var repositoryId: String = ""

    @Option(option = "dry", description = "dry")
    private var dry: Boolean = false

    @TaskAction
    fun postRequest() {
        val sourceRoot = "${project.name}/src/main/java/com/peterfarlow"
        val stackRoot = getStackRoot()
        val body = SentryBody(
            defaultBranch = defaultBranch,
            integrationId = integrationId,
            projectId = projectId,
            repositoryId = repositoryId,
            sourceRoot = sourceRoot,
            stackRoot = stackRoot
        )
        project.logger.debug("About to send mapping for ${project.name}\nsourceRoot: $sourceRoot\nstackRoot: $stackRoot\n---")
        if (dry) {
            return
        }
        runBlocking {
            KtorClientProvider.client.post("https://us.sentry.io/api/0/organizations/$slug/code-mappings/") {
                headers {
                    set("Authorization", "Bearer $token")
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }.also {
                logger.info(it.body<String>())
            }
        }
    }

    private fun getStackRoot(): String {
        val traceableExtension = project.extensions.findByType<TraceableExtension>().also { project.logger.debug("project ${project.name} set rootPackageName to ${it?.rootPackageName}") }
        return if (traceableExtension != null) {
            "com/peterfarlow/${traceableExtension.rootPackageName}"
        } else {
            "com/peterfarlow/${project.name}"
        }
    }
}


@Serializable
data class SentryBody(
    val defaultBranch: String = "main",
    val integrationId: String = "",
    val projectId: String = "",
    val repositoryId: String = "",
    val sourceRoot: String = "",
    val stackRoot: String = ""
)
