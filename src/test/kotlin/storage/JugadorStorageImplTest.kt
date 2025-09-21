package storage

import org.example.models.Jugador
import org.example.models.Posicion
import org.example.storage.JugadorStorageImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class JugadorStorageImplTest {

    val storage = JugadorStorageImpl()

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

    val lista = listOf(jugador1, jugador2)

    @Nested
    @DisplayName("Tests correctos")
    inner class TestsCorrectos {

        @Test
        @DisplayName("Importar jugadores")
        fun importarJugadores(@TempDir tempDir: File) {

            val file = File(tempDir, "data.csv")
            file.writeText("id,nombre,dorsal,posicion,club\n" +
            "1,Carlos,7,CENTROCAMPISTA,Atlético de Madrid\n" +
            "2,Samuel,10,DELANTERO,Real Madrid")

            val lista = storage.fileRead(file).value
            val jugador1Actual = lista.first()
            val jugador2Actual = lista.last()

            assertAll(
                { assertEquals(jugador1.id, jugador1Actual.id) },
                { assertEquals(jugador1.nombre, jugador1Actual.nombre) },
                { assertEquals(jugador1.dorsal, jugador1Actual.dorsal) },
                { assertEquals(jugador1.posicion, jugador1Actual.posicion) },
                { assertEquals(jugador1.club, jugador1Actual.club) },
                { assertEquals(jugador2.id, jugador2Actual.id) },
                { assertEquals(jugador2.nombre, jugador2Actual.nombre) },
                { assertEquals(jugador2.dorsal, jugador2Actual.dorsal) },
                { assertEquals(jugador2.posicion, jugador2Actual.posicion) },
                { assertEquals(jugador2.club, jugador2Actual.club) },
            )

        }
    }

    @Nested
    @DisplayName("Tests incorrectos")
    inner class TestsIncorrectos {

        @Test
        @DisplayName("No es un archivo")
        fun notFileOnImport(){
            val file = File("media/")
            val result = storage.fileRead(file)

            assertTrue(result.isErr)
            assertEquals(
                "Error en el storage: El fichero no existe, la ruta especificada no es un fichero o no se tienen permisos de lectura",
                result.error.message
            )
        }

        @Test
        @DisplayName("Archivo inexistente")
        fun fileDoesNotExist() {
            val file = File("ruta/que/no/existe.csv")
            val result = storage.fileRead(file)

            assertTrue(result.isErr)
            assertEquals(
                "Error en el storage: El fichero no existe, la ruta especificada no es un fichero o no se tienen permisos de lectura",
                result.error.message
            )
        }

    }

}