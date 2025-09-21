package org.example.validator

import org.example.errors.JugadorErrors
import org.example.models.Jugador

interface JugadorValidator: Validator<Jugador, JugadorErrors> {
}