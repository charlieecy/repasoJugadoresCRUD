package org.example.models

import java.time.LocalDateTime

/**
 * Clase que representa un [Jugador] en forma de entidad, para la base de datos.
 * @param id [Long] Id único del jugador
 * @param nombre [String] Nombre del jugador
 * @param dorsal [Int] Número del jugador
 * @param posicion [Posicion] Posición del jugador
 * @param club [String] Club del jugador
 * @param createdAt [LocalDateTime] Fecha y hora en que el jugador fue creado
 * @param updatedAt [LocalDateTime] Fecha y hora en que el jugador fue actualizado
 * @param isDeleted [Boolean] Si el jugador ha sido borrado
 */
data class JugadorEntity(
    val id: Long,
    val nombre: String,
    val dorsal: Int,
    val posicion: Posicion,
    val club: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false
)
