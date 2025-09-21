package org.example.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.errors.JugadorErrors
import org.example.models.Jugador
import org.example.models.Posicion

/**
 * Clase que representa el validador de objetos del tipo [Jugador]
 */
class JugadorValidatorImpl : JugadorValidator {

    /**
     * Valida un [Jugador].
     * @return El propio jugador si los datos son correctos, un [JugadorErrors.InvalidoError] en caso contrario.
     */
    override fun validate(entity: Jugador): Result<Jugador, JugadorErrors.InvalidoError> {
        val idResult = validateID(entity)
        if (idResult.isErr) return idResult

        val nameResult = validateName(entity)
        if (nameResult.isErr) return nameResult

        val dorsalResult = validateDorsal(entity)
        if (dorsalResult.isErr) return dorsalResult

        val clubResult = validateClub(entity)
        if (clubResult.isErr) return clubResult

        return Ok(entity)
    }

    /**
     * Valida el ID de un [Jugador].
     * @return El propio jugador si los datos son correctos, un [JugadorErrors.InvalidoError] en caso contrario.
     */
    private fun validateID(entity: Jugador): Result<Jugador, JugadorErrors.InvalidoError> {
        return if (entity.id < 1) {
            Err(JugadorErrors.InvalidoError("El id del jugador no puede ser menor a 1"))
        } else {
            Ok(entity)
        }
    }

    /**
     * Valida el nombre de un [Jugador].
     * @return El propio jugador si los datos son correctos, un [JugadorErrors.InvalidoError] en caso contrario.
     */
    private fun validateName(entity: Jugador): Result<Jugador, JugadorErrors.InvalidoError> {
        return if (entity.nombre.isEmpty()) {
            Err(JugadorErrors.InvalidoError("El nombre del jugador no puede estar vacío"))
        } else {
            Ok(entity)
        }
    }

    /**
     * Valida el dorsal de un [Jugador].
     * @return El propio jugador si los datos son correctos, un [JugadorErrors.InvalidoError] en caso contrario.
     */
    private fun validateDorsal(entity: Jugador): Result<Jugador, JugadorErrors.InvalidoError> {
        return if (entity.dorsal !in 1..99) {
            Err(JugadorErrors.InvalidoError("El dorsal del jugador debe estar comprendido entre 1 y 99"))
        } else {
            Ok(entity)
        }
    }

    /**
     * Valida el club de un [Jugador].
     * @return El propio jugador si los datos son correctos, un [JugadorErrors.InvalidoError] en caso contrario.
     */
    private fun validateClub(entity: Jugador): Result<Jugador, JugadorErrors.InvalidoError> {
        return if (entity.club.isEmpty()) {
            Err(JugadorErrors.InvalidoError("El club del jugador no puede estar vacío"))
        } else {
            Ok(entity)
        }
    }
}
