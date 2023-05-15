package pl.poznan.put.tsd.planningpoker.backend.components

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import pl.poznan.put.tsd.planningpoker.backend.model.InvalidFileException
import pl.poznan.put.tsd.planningpoker.backend.model.Task
import pl.poznan.put.tsd.planningpoker.backend.model.UserStory
import java.io.File
import kotlin.jvm.Throws

@Component
class CsvParser {
    @Throws(InvalidFileException::class)
    fun readCsv(file: MultipartFile): List<UserStory> {
        val userStories = mutableListOf<UserStory>()
        val tasks = mutableListOf<Task>()

        try {
            csvReader().open(file.inputStream) {

                readAllWithHeaderAsSequence().forEach {
                    if (it["Issue Type"].equals("Story"))
                        userStories.add(UserStory(it["Issue id"]!!.toInt(), it["Summary"]!!, mutableListOf(),
                            it["Original estimate"]?.toIntOrNull()
                        ))
                    else
                        tasks.add(Task(it["Issue id"]!!.toInt(), it["Summary"]!!, it["Parent"]!!.toInt(),
                            it["Parent summary"]!!))
                }
                tasks.forEach {
                    for (userStory: UserStory in userStories) {
                        if (userStory.id == it.parentId) {
                            userStory.tasks.add(it)
                            break
                        }
                    }
                }
            }
            return userStories
        } catch (e: NullPointerException) {
            throw InvalidFileException("Invalid file provided")
        }
    }

    fun writeToCsv(userStories: List<UserStory>) : File {
        csvWriter().open("Jira.csv") {
            writeRow(listOf("Summary", "Issue id", "Issue Type", "Original estimate", "Parent", "Parent summary"))

            var idCounter = 10000

            for (userStory: UserStory in userStories) {

                if (userStory.id == null)
                    userStory.id = idCounter++
                else
                    idCounter = userStory.id!! + 1

                writeRow(listOf(userStory.name, userStory.id, "Story", userStory.estimationAverage, "", ""))

                for (task: Task in userStory.tasks) {

                    if (task.id == null)
                        task.id = idCounter++
                    else
                        idCounter = task.id!! + 1

                    writeRow(listOf(task.description, task.id, "Subtask", "", userStory.id, userStory.name))
                }
            }
        }
        return File("Jira.csv")
    }
}
