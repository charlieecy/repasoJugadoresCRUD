package org.example.validator

import com.github.michaelbull.result.Result
import org.example.errors.JugadorErrors
import org.example.models.Jugador

interface Validator<T, E> {
    fun validate(entity: T): Result<T, E>
}