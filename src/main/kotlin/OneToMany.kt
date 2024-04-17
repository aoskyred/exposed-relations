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
        SchemaUtils.create(Book, Author)
    }

    transaction {
        Book
            .join(Author, JoinType.INNER, Book.id, Author.id)
            .selectAll()
    }
}

object Book : IntIdTable() {
    val name = varchar("name", Int.MAX_VALUE)
    val authorId = reference("id", Author)
}

object Author : IntIdTable() {
    val name = varchar("name", Int.MAX_VALUE)
}