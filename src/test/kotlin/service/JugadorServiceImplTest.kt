package service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import org.example.errors.JugadorErrors
import org.example.mappers.copy
import org.example.models.Jugador
import org.example.models.Posicion
import org.example.repository.JugadorRepository
import org.example.service.JugadorServiceImpl
import org.example.storage.JugadorStorage
import org.example.validator.JugadorValidator
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.*
import java.nio.file.Path

class JugadorServiceImplTest {

    private val validator: JugadorValidator = mock()
    private val repository: JugadorRepository = mock()
    private val storage: JugadorStorage = mock()
    private val cache: Cache<Long, Jugador> = mock()

    private lateinit var service: JugadorServiceImpl

    private val jugador1 = Jugador(
        id = 1L,
        nombre = "Carlos",
        dorsal = 7,
        posicion = Posicion.CENTROCAMPISTA,
        club = "Atlético de Madrid"
    )

    @BeforeEach
    fun setUp() {
        service = JugadorServiceImpl(repository, validator, storage, cache)
    }

    @Nested
    @DisplayName("Tests Correctos")
    inner class TestsCorrectos {

        @Test
        @DisplayName("Importar jugadores de fichero")
        fun importFromFileOk() {
            whenever(storage.fileRead(any())).thenReturn(Ok(listOf(jugador1)))
            whenever(repository.save(any())).thenReturn(jugador1)

            val result = service.importFromFile(Path.of("jugadores.json"))

            assertTrue(result.isOk)
            verify(repository, times(1)).save(jugador1)
        }

        @Test
        @DisplayName("Obtener todos lod jugadores")
        fun getAllOk() {
            whenever(repository.getAll()).thenReturn(listOf(jugador1))

            val result = service.getAll()

            assertEquals(1, result.size)
            assertEquals("Carlos", result[0].nombre)
            verify(repository, times(1)).getAll()
        }

        @Test
        @DisplayName("Buscar por ID (está en caché)")
        fun getByIdCache() {
            whenever(cache.getIfPresent(1L)).thenReturn(jugador1)

            val result = service.getById(jugador1.id)

            assertTrue(result.isOk)
            assertEquals("Carlos", result.value.nombre)
            verify(cache, times(1)).getIfPresent(jugador1.id)
            verify(repository, times(0)).getById(jugador1.id)
            verify(cache, times(0)).put(jugador1.id, jugador1)
        }


        @Test
        @DisplayName("Buscar por ID (no está en caché)")
        fun getByIdRepository() {
            whenever(cache.getIfPresent(1L)).thenReturn(null)
            whenever(repository.getById(1L)).thenReturn(jugador1)

            val result = service.getById(jugador1.id)

            assertTrue(result.isOk)
            verify(cache, times(1)).put(1L, jugador1)
            verify(repository, times(1)).getById(jugador1.id)

        }

        @Test
        @DisplayName("Guardar jugador")
        fun saveOk() {
            whenever(validator.validate(jugador1)).thenReturn(Ok(jugador1))
            whenever(repository.save(jugador1)).thenReturn(jugador1)

            val result = service.save(jugador1)

            assertTrue(result.isOk)
            verify(validator, times(1)).validate(jugador1)
            verify(repository, times(1)).save(jugador1)
        }

        @Test
        @DisplayName("Actualizar jugador")
        fun updateOk() {
            whenever(validator.validate(jugador1)).thenReturn(Ok(jugador1))
            whenever(repository.getById(1L)).thenReturn(jugador1)
            whenever(repository.update(jugador1, 1L)).thenReturn(jugador1)

            val result = service.update(jugador1, 1L)

            assertTrue(result.isOk)
            verify(validator, times(1)).validate(jugador1)
            verify(repository, times(1)).getById(1L)
            verify(repository, times(1)).update(jugador1, 1L)
            verify(cache, times(1)).put(1L, jugador1)
        }

        @Test
        @DisplayName("Borrar jugador")
        fun deleteOk() {
            whenever(repository.getById(1L)).thenReturn(jugador1)
            whenever(repository.deleteById(1L)).thenReturn(jugador1.copy(newIsDeleted = true))

            val result = service.delete(1L)

            assertTrue(result.isOk)
            verify(repository, times(1)).getById(1L)
            verify(repository, times(1)).deleteById(1L)
            verify(cache, times(1)).invalidate(1L)
        }
    }

    @Nested
    @DisplayName("Tests Incorrectos")
    inner class IncorrectTests {

        @Test
        @DisplayName("Error del storage al importar")
        fun importNotOk() {
            whenever(storage.fileRead(any())).thenReturn(Err(JugadorErrors.StorageError("fallo")))

            val result = service.importFromFile(Path.of("data.csv"))

            assertTrue(result.isErr)
            verify(storage, times(1)).fileRead(any())
            verify(repository, times(0)).save(any())
        }

        @Test
        @DisplayName("Jugador no encontrado por ID")
        fun getByIdNotFound() {
            whenever(cache.getIfPresent(1L)).thenReturn(null)
            whenever(repository.getById(1L)).thenReturn(null)

            val result = service.getById(1L)

            assertTrue(result.isErr)
            verify(cache, times(1)).getIfPresent(1L)
            verify(repository, times(1)).getById(1L)
        }

        @Test
        @DisplayName("Error de validación al guardar")
        fun saveInvalidJugador() {
            whenever(validator.validate(jugador1)).thenReturn(Err(JugadorErrors.InvalidoError("invalido")))

            val result = service.save(jugador1)

            assertTrue(result.isErr)

            verify(validator, times(1)).validate(jugador1)
            verify(repository, times(0)).save(jugador1)
        }

        @Test
        @DisplayName("Jugador no encontrado al actualizar")
        fun updateNotFound() {
            whenever(validator.validate(jugador1)).thenReturn(Ok(jugador1))
            whenever(repository.getById(1L)).thenReturn(null)

            val result = service.update(jugador1, 1L)

            assertTrue(result.isErr)
            verify(validator, times(1)).validate(jugador1)
            verify(repository, times(1)).getById(1L)
            verify(repository, times(0)).update(jugador1, 1L)
            verify(cache, times(1)).invalidate(1L)
            verify(cache, times(0)).put(1L, jugador1)

        }

        @Test
        @DisplayName("Jugador no encontrado al borrar")
        fun deleteNotFound() {
            whenever(repository.getById(1L)).thenReturn(null)

            val result = service.delete(1L)

            assertTrue(result.isErr)
            verify(repository, times(1)).getById(1L)
            verify(cache, times(1)).invalidate(1L)
            verify(repository, times(0)).deleteById(1L)
        }

        @Test
        @DisplayName("Jugador encontrado pero no pudo ser borrado")
        fun deleteFailure() {
            whenever(repository.getById(1L)).thenReturn(jugador1)
            whenever(repository.deleteById(1L)).thenReturn(null)

            val result = service.delete(1L)

            assertTrue(result.isErr)
            verify(repository, times(1)).getById(1L)
            verify(cache, times(1)).invalidate(1L)
            verify(repository, times(1)).deleteById(1L)        }
    }
}
