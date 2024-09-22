package io.github.josephsimutis

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.switch
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.InvalidParameterException

class TodoList : CliktCommand() {
    val configFile by argument(help="The JSON file that contains the list").file(canBeDir = false)
    val action by option(help="The action that the program will execute").switch(
        "--actions" to TodoAction.HELP,
        "--list" to TodoAction.LIST,
        "--add" to TodoAction.ADD,
        "--remove" to TodoAction.REMOVE,
        "--complete" to TodoAction.COMPLETE,
        "--uncomplete" to TodoAction.UNCOMPLETE
    ).required()
    val task by option(help="The task to change, if needed for the action")
    var tasks = ArrayList<Task>()

    override fun run() {
        if (configFile.exists()) {
            tasks = Json.decodeFromString<ArrayList<Task>>(configFile.readText())
        }
        action(this)
        configFile.writeText(Json.encodeToString(tasks))
    }

    @Serializable
    data class Task(val name: String, var complete: Boolean)

    enum class TodoAction(val action: (TodoList) -> Unit) {
        HELP({ list -> }),
        LIST({ list ->
            list.terminal.println("Tasks:")
            for (task in list.tasks) {
                list.terminal.println(task.name)
            }
        }),
        ADD({ list ->
            if (list.tasks.any { it.name == list.task }) throw InvalidParameterException("Cannot have duplicate tasks!")
            list.tasks += Task(list.task!!, false)
        }),
        REMOVE({ list ->
            list.tasks -= Task(list.task!!, false)
        }),
        COMPLETE({ list ->
            val index = list.tasks.indexOf(list.tasks.first { it.name == list.task })
            list.tasks[index].complete = true
        }),
        UNCOMPLETE({ list ->
            val index = list.tasks.indexOf(list.tasks.first { it.name == list.task })
            list.tasks[index].complete = false
        });

        operator fun invoke(list: TodoList) = action(list)
    }
}