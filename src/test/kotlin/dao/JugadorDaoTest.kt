package dao

import org.example.dao.JugadorDao
import org.example.dependencies.Dependencies
import org.example.models.JugadorEntity
import org.example.models.Posicion
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JugadorDaoTest {
    private lateinit var dao: JugadorDao

    val jugadorEntity = JugadorEntity(
        id = 1L,
        nombre = "Pikachu",
        dorsal = 12,
        posicion = Posicion.DELANTERO.toString(),
        club = "Atlético de Madrid",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        isDeleted = false
    )

    @BeforeAll
    fun setUp() {
        val jdbi = Dependencies.provideDatabaseManager()
        dao = Dependencies.provideJugadorDao(jdbi)
    }

    @AfterEach
    fun tearDown() {
        dao.deleteAll()
    }

    @Nested
    @DisplayName("Tests correctos")
    inner class DaoTestCorrectos {
        @Test
        @DisplayName("Guardar un jugador")
        fun savePersona() {
            val id = dao.save(jugadorEntity)
            val jugador = dao.getById(id.toLong())
            assertNotNull(jugador, "No debería ser nulo")
            assertEquals(jugador!!.nombre, jugador.nombre, "Deberían ser iguales")
        }

        @Test
        @DisplayName("Eliminar un jugador")
        fun eliminarPersona() {
            val id = dao.save(jugadorEntity)
            val result = dao.deleteById(id.toLong())
            val result2 = dao.getById(id.toLong())
            assertNull(result2, "No debería estar")
            assertEquals(1, result, "Debería haber cambiado a una jugador")
        }

        @Test
        @DisplayName("Obtener a todos los jugadores")
        fun obtenerPersonas() {
            dao.save(jugadorEntity)
            dao.save(jugadorEntity.copy(nombre = "Carlos"))
            dao.save(jugadorEntity.copy(club = "Real Sociedad"))

            val todos = dao.getAll()

            val result = dao.getAll()
            assertEquals(result.size, 3, "Debería haber tres jugadores")
            assertTrue(result.any { it.nombre == "Pikachu" }, "Debería haber un jugador con ese nombre")
            assertTrue(result.any { it.nombre == "Carlos" }, "DeberÍa haber un jugador con ese nombre")
            assertTrue(result.any { it.club == "Real Sociedad" }, "Debería haber un jugador de ese equipo")
        }

        @Test
        @DisplayName("Actualizar correctamente")
        fun actualizarPersona() {
            val id = dao.save(jugadorEntity)
            val result = dao.getById(id.toLong())

            val result2 = dao.update(jugadorEntity.copy(nombre = "Carlos"), id.toLong())
            val result3 = dao.getById(id.toLong())
            assertNotEquals(result!!.nombre, result3!!.nombre, "No deberian ser iguales")
            assertTrue(result2 == 1)

        }

        @Test
        @DisplayName("Obtener por id (existe)")
        fun obtenerPersona() {
            val id = dao.save(jugadorEntity)
            val result = dao.getById(id.toLong())
            assertNotNull(result, "No debería ser nulo")
            assertEquals(result!!.nombre, jugadorEntity.nombre, "Debería tener el mismo nombre")

        }

        @Test
        @DisplayName("Eliminar a todos las personas")
        fun eliminarPersonas() {
            dao.save(jugadorEntity)
            dao.save(jugadorEntity.copy(club = "Real Sociedad"))
            val result = dao.deleteAll()
            val result2 = dao.getAll()
            assertTrue(result2.isEmpty(), "Debería no haber ninguno")
            assertEquals(result, 2, "Deberían haberse modificado sólo dos líneas")
        }
    }

    @Nested
    @DisplayName("Tests incorrectos")
    inner class DaoTestIncorrectos {
        @Test
        @DisplayName("Obtener todos (vacío)")
        fun getVacio() {
            val result = dao.getAll()
            assertTrue(result.isEmpty(), "Deberia estar vacío")
            assertEquals(result.size, 0, "Debería ser cero")
        }


        @Test
        @DisplayName("Obtener por id (no existe)")
        fun obtenerIdNoEstando() {
            dao.save(jugadorEntity)
            val result = dao.getById(565L)
            assertNull(result, "Debería ser nulo")
        }

        @Test
        @DisplayName("Actualizar por id (no existe)")
        fun actualizarPorIdNoEstando() {
            dao.save(jugadorEntity)
            val result = dao.update(jugadorEntity.copy(id = 37L), id = 1L)
            assertTrue(result==0,"No debería haber modificado ninguna fila")
        }

        @Test
        @DisplayName("eliminar no estando")
        fun eliminarNoEstando() {
            dao.save(jugadorEntity)
            val result = dao.deleteById(565L)
            assertTrue(result == 0, "No debería haber modificado ninguna fila")

        }
    }
}