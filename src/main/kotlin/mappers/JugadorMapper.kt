package org.example.mappers

import org.example.models.Jugador
import org.example.models.JugadorEntity
import org.example.models.Posicion
import java.time.LocalDate
import java.time.LocalDateTime


/**
     * Parsea un [JugadorEntity] a [Jugador]
     * @return El [Jugador]
     */
    fun JugadorEntity.toModel (): Jugador {
        return Jugador(
            id = id,
            nombre = nombre,
            dorsal = dorsal,
            posicion = Posicion.valueOf(posicion),
            club = club,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isDeleted = isDeleted
        )
    }

    /**
     * Parsea un [Jugador] a [JugadorEntity]
     * @return El [JugadorEntity]
     */
    fun Jugador.toEntity (): JugadorEntity {
        return JugadorEntity(
            id = id,
            nombre = nombre,
            dorsal = dorsal,
            posicion = posicion.toString(),
            club = club,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isDeleted = isDeleted
        )
    }

/**
 * Crea una copia de un objeto de la clase [Jugador]
 * @param newId Nuevo id que recibira el objeto en la copia si desea actualizar, por defecto el mismo que antes
 * @param newNombre Nuevo nombre que recibira el objeto en la copia si desea actualizar, por defecto el mismo que antes
 * @param newPosicion Nueva posicion que recibira el objeto en la copia si desea actualizar, por defecto la misma que antes
 * @param newDorsal Nueva dorsal que recibira el objeto en la copia si desea actualizar, por defecto la misma que antes
 * @param newClub Nueva dorsal que recibira el objeto en la copia si desea actualizar, por defecto la misma que antes
 * @param newCreatedAt Fecha de creación que recibira el objeto en la copia si desea actualizar, por defecto la misma que antes
 * @param newUpdatedAt Fecha de creación que recibira el objeto en la copia si desea actualizar, por defecto la misma que antes
 * @param newIsDeleted Nuevo borrado lógico que recibira el objeto en la copia si desea actualizar, por defecto el mismo que antes
 * @return La copia del objeto creado
 */
fun Jugador.copy(
    newId: Long= this.id,
    newNombre: String= this.nombre,
    newPosicion: Posicion = this.posicion,
    newDorsal: Int = this.dorsal,
    newClub: String= this.club,
    newCreatedAt: LocalDateTime = this.createdAt,
    newUpdatedAt: LocalDateTime = this.updatedAt,
    newIsDeleted: Boolean = this.isDeleted,
): Jugador {
    return Jugador(
        id = newId,
        nombre = newNombre,
        posicion = newPosicion,
        dorsal = newDorsal,
        club = newClub,
        createdAt = newCreatedAt,
        updatedAt = newUpdatedAt,
        isDeleted = newIsDeleted,
    )
}