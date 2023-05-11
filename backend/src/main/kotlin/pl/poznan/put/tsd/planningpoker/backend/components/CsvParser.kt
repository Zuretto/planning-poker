package pl.poznan.put.tsd.planningpoker.backend.components

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import pl.poznan.put.tsd.planningpoker.backend.model.Task
import pl.poznan.put.tsd.planningpoker.backend.model.UserStory

@Component
class CsvParser {
    fun readCsv(file: MultipartFile): List<UserStory> {
        val userStories = mutableListOf<UserStory>()
        val tasks = mutableListOf<Task>()

        csvReader().open(file.inputStream) {

            readAllWithHeaderAsSequence().forEach {
                if (it["Issue Type"].equals("Story"))
                    userStories.add(UserStory(it["Issue key"]!!, it["Summary"]!!, mutableListOf()))
                else
                    tasks.add(Task(it["Issue key"]!!, it["Summary"]!!, it["Parent summary"]!!))
            }
            tasks.forEach {
                for (userStory: UserStory in userStories) {
                    if (userStory.name.equals(it.parentSummary)) {
                        userStory.tasks.add(it)
                        break
                    }
                }
            }
        }
        return userStories
    }
}