package io.github.manuelernesto.onlinejavasummitdemo

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * @author  Manuel Ernesto (manuelernest0)
 * @date  07/02/23 11:15 PM
 * @version 1.0
 */

@Table("speaker")
data class Speaker(
    @Id var id: Int?,
    val name: String,
    val company: String,
    val talk: String
)


@Repository
interface SpeakerRepository : CoroutineCrudRepository<Speaker, Int>{
    @Query("SELECT * FROM speaker")
    suspend fun getAll(): List<Speaker>
}

@Service
class SpeakerService(private val repository: SpeakerRepository) {
    suspend fun save(speaker: Speaker) = repository.save(speaker)
    suspend fun get() = repository.getAll()

    suspend fun get(id: Int): Speaker? {
        return if (repository.existsById(id)) {
            repository.findById(id)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    suspend fun update(speaker: Speaker, id: Int): Speaker {
        return if (repository.existsById(id)) {
            speaker.id = id
            repository.save(speaker)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    suspend fun delete(id: Int) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

}

@RestController
@RequestMapping("/api/v1/speakers")
class SpeakerController(private val service: SpeakerService) {

    @GetMapping
    suspend fun getAllSpeakers() = service.get()

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: Int) = service.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun save(@RequestBody speaker: Speaker) =
        service.save(speaker)

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: Int, @RequestBody speaker: Speaker) = service.update(speaker, id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(@PathVariable id: Int) = service.delete(id)

}
