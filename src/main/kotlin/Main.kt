package io.github.josephsimutis

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.versionOption

fun main(args: Array<String>) = TodoList().versionOption("1.0.0-pre.1").main(args)