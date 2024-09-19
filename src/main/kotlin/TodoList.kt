package io.github.josephsimutis

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.switch
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.json.Json

class TodoList : CliktCommand() {
    val configFile by argument(help="The JSON file that contains the list").file(canBeDir = false).optional()
    val action by option(help="The action that the program will execute").switch(
        "--actions" to TodoAction.HELP,
        "--list" to TodoAction.LIST,
    ).required()

    override fun run() {
        val tasks = if (configFile?.exists() == true) {
            Json.decodeFromString<ArrayList<Task>>(configFile!!.readText())
        } else {
            ArrayList()
        }
        action(this, tasks)
    }

    data class Task(val name: String)

    enum class TodoAction(val action: (TodoList, ArrayList<Task>) -> Unit) {
        HELP({ list, tasks -> }),
        LIST({ list, tasks ->
            list.terminal.println("Tasks:")
            for (task in tasks) {
                list.terminal.println(task.name)
            }
        });

        operator fun invoke(list: TodoList, tasks: ArrayList<Task>) = action(list, tasks)
    }
}