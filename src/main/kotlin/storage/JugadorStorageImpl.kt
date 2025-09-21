package org.example.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.dto.JugadorDTO
import org.example.errors.JugadorErrors
import org.example.mappers.toModel
import org.example.models.Jugador
import java.io.File

class JugadorStorageImpl: JugadorStorage {
    override fun fileRead(file: File): Result<List<Jugador>, JugadorErrors.StorageError> {
        if (!file.exists() || !file.isFile || !file.canRead()) {
            return Err(JugadorErrors.StorageError("El fichero no existe, la ruta especificada no es un fichero o no se tienen permisos de lectura"))
        }

        return Ok( file.readLines()
            .drop(1)
            .map{it.split(",")}
            .map{
                JugadorDTO(
                    id = it[0].toLong(),
                    nombre = it[1],
                    dorsal = it[2].toInt(),
                    posicion = it[3],
                    club = it[4],
                ).toModel()
            }
        ) // Fin Ok
    }
}