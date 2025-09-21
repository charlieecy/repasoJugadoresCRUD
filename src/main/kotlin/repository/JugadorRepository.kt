package org.example.repository

import org.example.models.Jugador

/**
 * Interfaz que implementa [CrudRepository], concretándola para esta aplicación
 */
interface JugadorRepository : CrudRepository<Long, Jugador> {
}