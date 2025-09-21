package org.example.storage

import org.example.errors.JugadorErrors
import org.example.models.Jugador

/**
 * Interfaz que representa el storage de [Jugador]
 */
interface JugadorStorage: Storage<Jugador, JugadorErrors.StorageError> {
}