package com.dreifus.app.data.notes

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.dreifus.app.data.notes.db.EventEntity
import com.dreifus.app.data.notes.db.NotesDatabaseHolder
import com.dreifus.app.data.notes.model.Event
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

/**
 * Events are not archived explicitly: anything with [Event.at] before "now" is archive,
 * anything at or after is upcoming. Queries take the split moment as a parameter.
 */
@Inject
@SingleIn(AppScope::class)
class EventsRepository(
    dbHolder: NotesDatabaseHolder,
) {

    private val queries = dbHolder.db.eventQueries

    fun observeUpcoming(from: Long): Flow<List<Event>> =
        queries.selectUpcoming(from)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(EventEntity::toDomain) }

    fun observeNextUpcoming(from: Long): Flow<Event?> =
        queries.selectNextUpcoming(from)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { it?.toDomain() }

    fun observePast(before: Long): Flow<List<Event>> =
        queries.selectPast(before)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(EventEntity::toDomain) }

    suspend fun getById(id: Long): Event? =
        queries.selectById(id).executeAsOneOrNull()?.toDomain()

    suspend fun insert(title: String, at: Long, color: String) {
        queries.insert(
            title = title,
            at = at,
            color = color,
            created_at = Clock.System.now().toEpochMilliseconds(),
        )
    }

    suspend fun update(id: Long, title: String, at: Long) {
        queries.update(title = title, at = at, id = id)
    }

    suspend fun delete(id: Long) {
        queries.delete(id)
    }
}

private fun EventEntity.toDomain() = Event(
    id = id,
    title = title,
    at = at,
    color = color,
    createdAt = created_at,
)
