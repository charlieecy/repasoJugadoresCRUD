package org.example.repository

/**
 * Interfaz que tiene las operaciones CRUD
 */
interface CrudRepository <ID, T> {
    fun getAll(): List<T>
    fun getById(id: ID): T?
    fun save(entity: T): T
    fun update(entity: T, id: ID): T?
    fun deleteById(id: ID): T?
}