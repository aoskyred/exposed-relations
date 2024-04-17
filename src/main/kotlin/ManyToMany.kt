package io.aosky

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val database = Database.connect("jdbc:sqlite:/data.db", "org.sqlite.JDBC")
    TransactionManager.defaultDatabase = database

    transaction {
        SchemaUtils.create(Movies, Actors, MoviesActors)
    }
    transaction {
        // Query Many To Many
        MoviesActors
            .join(Actors, JoinType.INNER, Actors.id, MoviesActors.actorId)
            .join(Movies, JoinType.INNER, Movies.id, MoviesActors.movieId)
            .selectAll()
    }
}



object Movies : IntIdTable() {
    val name = varchar("name", Int.MAX_VALUE)
}

object Actors : IntIdTable() {
    val name = varchar("name", Int.MAX_VALUE)
}

object MoviesActors : Table() {
    val movieId = reference("movie_id", Movies, onDelete = ReferenceOption.CASCADE)
    val actorId = reference("actor_id", Actors, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(actorId, movieId, name = "PK_Key")

    init {
        index(true, actorId, movieId)
    }
}