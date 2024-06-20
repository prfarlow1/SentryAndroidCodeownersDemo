package com.peterfarlow.traceable

import org.jetbrains.kotlin.gradle.utils.property

abstract class TraceableExtension {
    var rootPackageName by property { "" }
}
