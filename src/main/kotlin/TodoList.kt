package io.github.josephsimutis

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.json.Json

class TodoList : CliktCommand() {
    val configFile by argument(help="The JSON file that contains the list").file(canBeDir = false).optional()

    override fun run() {
        val config = if (configFile != null && configFile?.exists() == true) {
            Json.decodeFromString<Config>(configFile!!.readText())
        } else {
            Config(ArrayList())
        }
    }

    data class Config(val tasks: ArrayList<String>)
}