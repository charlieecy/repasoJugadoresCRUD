package org.example.storage

import com.github.michaelbull.result.Result
import java.io.File

/**
 * Interfaz que representa el storage
 */
interface Storage<T, E> {
    fun fileRead(file: File): Result<List<T>, E>
}