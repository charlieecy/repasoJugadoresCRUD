package repository

import org.example.dao.JugadorDao
import org.example.mappers.toEntity
import org.example.models.Jugador
import org.example.models.Posicion
import org.example.repository.JugadorRepositoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.internal.verification.VerificationModeFactory.times
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class JugadorRepositoryImplTest {

    @Mock
    private lateinit var dao: JugadorDao

    @InjectMocks
    private lateinit var repository: JugadorRepositoryImpl

    private val jugador1 = Jugador(
        id = 1L,
        nombre = "Carlos",
        dorsal = 7,
        posicion = Posicion.CENTROCAMPISTA,
        club = "Atlético de Madrid",
    )

    private val jugador2 = Jugador(
        id = 2L,
        nombre = "Samuel",
        dorsal = 10,
        posicion = Posicion.DELANTERO,
        club = "Real Madrid",
    )

    private val jugador3 = Jugador(
        id = 3L,
        nombre = "Jesús",
        dorsal = 1,
        posicion = Posicion.PORTERO,
        club = "F. C. Barcelona",
    )

    private val jugador4 = Jugador(
        id = 4L,
        nombre = "Víctor",
        dorsal = 3,
        posicion = Posicion.DEFENSA,
        club = "Villarreal",
    )

    @Nested
    @DisplayName("Tests correctos")
    inner class TestsCorrectos {

        @Test
        @DisplayName("Obtener todos los jugadores")
        fun getAll(){
            whenever(dao.getAll()) doReturn listOf(jugador1.toEntity(), jugador2.toEntity(), jugador3.toEntity(), jugador4.toEntity())

            val result = repository.getAll()

            assertEquals(4, result.size, "Debería tener cuatro items")
            assertNotNull(result, "No debería estar vacío")

            verify(dao, times(1)).getAll()
        }

        @Test
        @DisplayName("Obtener por id")
        fun getByID(){
            whenever(dao.getById(2)) doReturn jugador2.toEntity()

            val result = repository.getById(jugador2.id)

            assertEquals(jugador2.nombre, result!!.nombre, "Deberían tener el mismo nombre")
            assertNotNull(result, "No debería ser nulo")
            assertEquals(jugador2.id, result.id, "Debería ser el mismo")

            verify(dao, times(1)).getById(2)
        }

        @Test
        @DisplayName("Guardar jugador")
        fun save(){
            whenever(dao.save(jugador1.toEntity())) doReturn 1

            val result = repository.save(jugador1)

            assertEquals(jugador1.id, result.id, "Debería coincidir")
            assertEquals(jugador1.nombre, result.nombre, "Debería coincidir")

            verify(dao, times(1)).save(jugador1.toEntity())
        }

        @Test
        @DisplayName("Actualizar jugador")
        fun update(){
            whenever(dao.update(jugador2.toEntity(), 2)).thenReturn(1)

            val result = repository.update(jugador2, jugador2.id)

            assertEquals(jugador2.id, result!!.id)
            assertEquals(jugador2.nombre, result.nombre)

            verify(dao, times(1)).update(jugador2.toEntity(), 2)
        }

        @Test
        @DisplayName("Borrar jugador por ID")
        fun deleteById(){
            whenever(dao.getById(jugador3.id)) doReturn jugador3.toEntity()
            whenever(dao.deleteById(jugador3.id)) doReturn 1

            val result = repository.deleteById(jugador3.id)

            assertEquals(jugador3.nombre, result!!.nombre)
            assertEquals(jugador3.id, result.id)
            assertNotEquals(jugador3.isDeleted, result.isDeleted)

            verify(dao, times(1)).deleteById(jugador3.id)
        }
    }

    @Nested
    @DisplayName("Tests incorrectos")
    inner class TestsIncorrectos {
        @Test
        @DisplayName("Obtener por id (NO EXISTE)")
        fun getByIdNotExists(){
            whenever(dao.getById(jugador3.id)) doReturn null

            val result = repository.getById(jugador3.id)

            assertEquals(null, result, "Debería ser nulo")

            verify(dao, times(1)).getById(jugador3.id)
        }

        @Test
        @DisplayName("Actualizar jugador (NO EXISTE)")
        fun updateNotExists(){
            whenever(dao.update(jugador4.toEntity(), jugador4.id)) doReturn 0

            val result = repository.update(jugador4, jugador4.id)

            assertEquals(null, result)

            verify(dao, times(1)).update(jugador4.toEntity(), jugador4.id)
        }

        @Test
        @DisplayName("Borrar jugador (NO EXISTE)")
        fun deleteNotExists(){
            whenever(dao.getById(jugador4.id)) doReturn null

            val result = repository.deleteById(jugador4.id)

            assertEquals(null, result, "Debería ser nulo")
            verify(dao, times(1)).getById(jugador4.id)
            verify(dao, times(0)).deleteById(jugador4.id)
        }

        @Test
        @DisplayName("Borrar jugador (falla la BBDD)")
        fun deleteBBDDFailure(){
            whenever(dao.getById(jugador4.id)) doReturn jugador4.toEntity()
            whenever(dao.deleteById(jugador4.id)) doReturn 0

            val result = repository.deleteById(jugador4.id)

            assertEquals(null, result, "Debería ser nulo")
            verify(dao, times(1)).getById(jugador4.id)
            verify(dao, times(1)).deleteById(jugador4.id)
            
        }
    }

}