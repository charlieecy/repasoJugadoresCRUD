package org.example.dto

import org.example.models.Posicion

/**
 * Data Transfer Object que representa el modelo de [org.example.models.Jugador], para poder ser importados desde ficheros
 */
data class JugadorDTO(
    val id: Long,
    val nombre: String,
    val dorsal: Int,
    val posicion: String,
    val club: String,
) {
}