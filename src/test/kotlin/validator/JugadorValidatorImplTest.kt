package validator

import org.example.mappers.copy
import org.example.models.Jugador
import org.example.models.Posicion
import org.example.validator.JugadorValidatorImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class JugadorValidatorImplTest {

    private val validator = JugadorValidatorImpl()

    private val jugadorValido = Jugador(
        id = 1L,
        nombre = "Messi",
        dorsal = 10,
        posicion = Posicion.DELANTERO,
        club = "PSG",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Nested
    @DisplayName("Tests correctos")
    inner class TestsCorrectos {
        @Test
        @DisplayName("Jugador válido pasa todas las validaciones")
        fun jugadorValido() {
            val result = validator.validate(jugadorValido)

            assertTrue(result.isOk, "El resultado debería ser Ok")
        }
    }

    @Nested
    @DisplayName("Tests incorrectos")
    inner class TestsIncorrectos {
        @Test
        @DisplayName("ID inválido (menor que 1)")
        fun idInvalido() {
            val jugador = jugadorValido.copy(newId = 0)
            val result = validator.validate(jugador)

            assertTrue(result.isErr, "El resultado debería ser Err")
            assertEquals("Jugador no válido: El id del jugador no puede ser menor a 1",result.error.message)
        }

        @Test
        @DisplayName("Nombre vacío")
        fun nombreVacio() {
            val jugador = jugadorValido.copy(newNombre = "")
            val result = validator.validate(jugador)

            assertTrue(result.isErr, "El resultado debería ser Err")
            assertEquals("Jugador no válido: El nombre del jugador no puede estar vacío",result.error.message)

        }

        @Test
        @DisplayName("Dorsal mayor a 99")
        fun dorsalSuperior() {
            val jugador = jugadorValido.copy(newDorsal = 150)
            val result = validator.validate(jugador)

            assertTrue(result.isErr, "El resultado debería ser Err")
            assertEquals("Jugador no válido: El dorsal del jugador debe estar comprendido entre 1 y 99",result.error.message)

        }

        @Test
        @DisplayName("Dorsal menor a 1")
        fun dorsalInferior() {
            val jugador = jugadorValido.copy(newDorsal = 0)
            val result = validator.validate(jugador)

            assertTrue(result.isErr, "El resultado debería ser Err")
            assertEquals("Jugador no válido: El dorsal del jugador debe estar comprendido entre 1 y 99",result.error.message)

        }

        @Test
        @DisplayName("Club vacío")
        fun clubVacio() {
            val jugador = jugadorValido.copy(newClub = "")
            val result = validator.validate(jugador)

            assertTrue(result.isErr, "El resultado debería ser Err")
            assertEquals("Jugador no válido: El club del jugador no puede estar vacío",result.error.message)

        }
    }
}
