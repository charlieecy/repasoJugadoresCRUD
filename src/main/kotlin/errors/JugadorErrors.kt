package org.example.errors

/**
 * Clase que representa los errores que pueden darse durante la ejecución del programa
 * @see [InvalidoError]
 * @see [StorageError]
 * @see [NotFoundError]
 */
sealed class JugadorErrors(val message: String) {
    /**
     * Error lanzado cuando sucede un error en la validación de datos del jugador
     */
    class InvalidoError (message: String) : JugadorErrors ("Jugador no válido: $message")
    /**
     * Error lanzado cuando sucede un error en el la escritura o lectura de archivos por parte del Storage
     */
    class StorageError (message: String) : JugadorErrors ("Error en el storage: $message")
    /**
     * Error lanzado cuando un integrante no es encontrado en la base de datos
     */
    class NotFoundError (message: String) : JugadorErrors ("Jugador no encontrado: $message")
}