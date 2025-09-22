package org.example.repository

import org.example.dao.JugadorDao
import org.example.mappers.copy
import org.example.mappers.toEntity
import org.example.models.Jugador
import org.example.mappers.toModel
import org.lighthousegames.logging.logging
import kotlin.math.log

/**
 * Clase que representa el repositorio de jugadores.
 */
class JugadorRepositoryImpl(
    private val dao: JugadorDao
) : JugadorRepository {

    private val logger = logging()

    override fun getAll(): List<Jugador> {
        logger.debug { "REPOSITORY: Obteniendo todos los jugadores" }

        return dao.getAll().map { it.toModel() }
    }

    override fun getById(id: Long): Jugador? {
        logger.debug { "REPOSITORY: Obteniendo jugador con id $id" }

        return dao.getById(id)?.toModel()
    }

    override fun save(entity: Jugador): Jugador {
        logger.debug { "REPOSITORY: Guardando jugador: $entity" }

        val idGenerated = dao.save(entity.toEntity())
        val savedJugador = entity.copy(newId = idGenerated.toLong())

        return savedJugador
    }

    override fun update(entity: Jugador, id: Long): Jugador? {
        logger.debug { "REPOSITORY: Actualizando jugador: $entity, con id: $id" }

        val result = dao.update(entity.toEntity(), id)

        if (result == 1) {
            return entity
        }
        return null
    }

    override fun deleteById(id: Long): Jugador? {
        logger.debug { "REPOSITORY: Borrando jugador con id: $id" }

        val entity = dao.getById(id)
        if (entity == null) {
            return null
        }

        val jugador = entity.toModel()
        val result = dao.deleteById(id)

        return if (result == 1) {
            jugador.copy(newIsDeleted = true)
        } else {
            null
        }
    }


}