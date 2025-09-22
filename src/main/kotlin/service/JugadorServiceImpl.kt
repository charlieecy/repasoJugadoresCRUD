package org.example.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.dao.JugadorDao
import org.example.errors.JugadorErrors
import org.example.models.Jugador
import org.example.repository.JugadorRepository
import org.example.storage.JugadorStorage
import org.example.validator.JugadorValidator
import org.lighthousegames.logging.logging
import java.nio.file.Path

class JugadorServiceImpl(
    private val repository: JugadorRepository,
    private val validator: JugadorValidator,
    private val storage: JugadorStorage,
    private val cache: Cache<Long, Jugador>
) : JugadorService {
    private val logger = logging()

    override fun importFromFile(file: Path): Result<List<Jugador>, JugadorErrors>{
        logger.debug { "SERVICE: Importando jugadores desde fichero $file" }

        val equipo = storage.fileRead(file.toFile())

        if (equipo.isErr){
            return equipo
        } else {
            equipo.value.forEach { repository.save(it) }
            return Ok(equipo.value)
        }
    }

    override fun getAll(): List<Jugador> {
        logger.debug { "SERVICE: Obteniendo todos los jugadores" }
        return repository.getAll()
    }

    override fun getById(id: Long): Result<Jugador, JugadorErrors> {
        logger.debug { "SERVICE: Obteniendo jugador con id: $id" }

        cache.getIfPresent(id)?.let {
            return Ok(it)
        }

        val jugadorBuscado = repository.getById(id)

        if (jugadorBuscado != null) {
            cache.put(id, jugadorBuscado)
            return Ok(jugadorBuscado)
        } else {
            return Err(JugadorErrors.NotFoundError("No ha podido encontrarse el jugador con id: $id"))
        }
    }

    override fun save(entity: Jugador): Result<Jugador, JugadorErrors> {
        logger.debug { "SERVICE: Guardando jugador: $entity" }

        val validation = validator.validate(entity)
        if (validation.isErr){
            return validation
        }

        return Ok(repository.save(entity))
    }

    override fun update(
        entity: Jugador,
        id: Long
    ): Result<Jugador, JugadorErrors> {
        logger.debug { "SERVICE: Actualizando jugador: $entity" }

        val validation = validator.validate(entity)
        if (validation.isErr){
            return validation
        }

        val jugadorToUpdate = repository.getById(id)
        if (jugadorToUpdate == null) {
            cache.invalidate(id)
            return Err(JugadorErrors.NotFoundError("No ha podido actualizarse el jugador con id: $id"))
        }

        val updatedJugador = repository.update(entity, id)
        cache.put(updatedJugador!!.id, updatedJugador)
        return Ok(updatedJugador)
    }

    override fun delete(id: Long): Result<Jugador, JugadorErrors> {
        logger.debug { "SERVICE: Borrando jugador con id: $id" }

        val jugadorToDelete = repository.getById(id)
        if (jugadorToDelete == null) {
            cache.invalidate(id)
            return Err(JugadorErrors.NotFoundError("No ha podido borrarse el jugador con id: $id"))
        }

        val deletedJugador = repository.deleteById(id)
        cache.invalidate(id)

        return if (deletedJugador != null) {
            Ok(deletedJugador)
        } else {
            Err(JugadorErrors.NotFoundError("No ha podido borrarse el jugador con id: $id"))
        }
    }

}