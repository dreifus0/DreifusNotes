package com.dreifus.app.data.notes.model

data class Event(
    val id: Long,
    val title: String,
    /** Event moment as epoch milliseconds. Events in the past are considered archived. */
    val at: Long,
    val color: String,
    val createdAt: Long,
)
