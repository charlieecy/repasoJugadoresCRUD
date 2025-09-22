package org.example.repository

import org.example.dao.JugadorDao
import org.example.mappers.copy
import org.example.mappers.toEntity
import org.example.models.Jugador
import org.example.mappers.toModel

class JugadorRepositoryImpl(
    private val dao: JugadorDao
) : JugadorRepository {

    override fun getAll(): List<Jugador> {
        return dao.getAll().map { it.toModel() }
    }

    override fun getById(id: Long): Jugador? {
        return dao.getById(id)?.toModel()
    }

    override fun save(entity: Jugador): Jugador {
        val idGenerated = dao.save(entity.toEntity())
        val savedJugador = entity.copy(newId = idGenerated.toLong())

        return savedJugador
    }

    override fun update(entity: Jugador, id: Long): Jugador? {
        val result = dao.update(entity.toEntity(), id)

        if (result == 1) {
            return entity
        }
        return null
    }

    override fun deleteById(id: Long): Jugador? {
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