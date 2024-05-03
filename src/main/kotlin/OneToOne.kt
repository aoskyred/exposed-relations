package io.aosky

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val database = Database.connect("jdbc:sqlite:/data.db", "org.sqlite.JDBC")
    TransactionManager.defaultDatabase = database

    transaction {
        SchemaUtils.create(Anime, Studio)
    }

    transaction {
        Anime.join(Studio, JoinType.INNER, Anime.id, Studio.id)
            .selectAll()
            .where {
                Studio.id eq Anime.studioId
            }
    }
}

object Anime : IntIdTable() {
    val name = varchar("name", Int.MAX_VALUE)
    val studioId = integer("studio_id").uniqueIndex() // add .nullable(), if you need.
}

object Studio : IntIdTable() {
    val name = varchar("name", Int.MAX_VALUE)
}