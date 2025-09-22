package org.example.service

import com.github.michaelbull.result.Result
import org.example.errors.JugadorErrors
import org.example.models.Jugador
import java.nio.file.Path

interface JugadorService: Service<Long, Jugador, JugadorErrors> {
    fun importFromFile(file: Path): Result<List<Jugador>, JugadorErrors>

}