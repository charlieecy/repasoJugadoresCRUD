package org.example.service

import com.github.michaelbull.result.Result

/**
 * Interfaz que represeta las operaciones CRUD de un servicio
 */
interface Service <ID, T, E> {
    fun getAll(): List<T>
    fun getById(id: ID): Result<T, E>
    fun save(entity: T): Result<T, E>
    fun update(entity: T, id: ID): Result<T, E>
    fun delete(id: ID): Result<T, E>
}