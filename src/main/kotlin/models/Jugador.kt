package org.example.models

import java.time.LocalDateTime

class Jugador (
    val nombre: String,
    val dorsal: Int,
    val posicion: Posicion,
    val club: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false
) {

    /**
     * Sobreescribe la funcion [toString] predeterminada dándole un formato más legible
     */
    override fun toString(): String {
        return "Jugador (nombre = $nombre, dorsal = $dorsal, posición = $posicion, club = $club,  createdAt= $createdAt, updatedAt= $updatedAt, isDeleted = $isDeleted)"
    }

}