package org.example.dependencies

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.example.dao.JugadorDao
import org.example.database.JdbiManager
import org.example.models.Jugador
import org.example.repository.JugadorRepository
import org.example.repository.JugadorRepositoryImpl
import org.example.service.JugadorService
import org.example.service.JugadorServiceImpl
import org.example.storage.JugadorStorage
import org.example.storage.JugadorStorageImpl
import org.example.validator.JugadorValidator
import org.example.validator.JugadorValidatorImpl
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging
import java.util.concurrent.TimeUnit


/**
 * Objeto que representa la inyección de las distintas dependencias necesarias para el funcionamiento de la aplicación.
 * @see [JdbiManager]
 * @see [JugadorDao]
 */
object Dependencies {

    private val logger = logging()

    init {
        logger.debug { "Inicializando gestor de dependencias" }
    }

    /**
     * Provee un JDBI
     * @return [Jdbi]
     */
    fun provideDatabaseManager(): Jdbi {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando JDBI" }
        return JdbiManager.instance
    }

    /**
     * Provee un JugadorDAO
     * @return [JugadorDao]
     */
    fun provideJugadorDao(jdbi: Jdbi): JugadorDao {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando DAO de Jugadores" }
        return jdbi.onDemand(JugadorDao::class.java)
    }

    /**
     * Provee un Repositorio de Jugadores
     * @return [JugadorRepositoryImpl]
     */
    fun provideJugadorRepository(dao: JugadorDao): JugadorRepository{
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando Repositorio de Jugadores" }
        return JugadorRepositoryImpl(dao)
    }

    /**
     * Provee un validador de Jugadores
     * @return[JugadorValidatorImpl]
     */
    private fun provideJugadorValidator(): JugadorValidator {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando Validador de Jugadores" }
        return JugadorValidatorImpl()
    }

    /**
     * Provee un storage de Jugadores
     * @return [JugadorStorageImpl]
     */
    private fun provideJugadorStorage(): JugadorStorage {
        logger.debug { "INYECCIÓN DE DEPENDENCIAS: Proporcionando Storage de Jugadores" }
        return JugadorStorageImpl()
    }

    /**
     * Provee una Cache de Jugadores
     * @return[Caffeine]
     */
    private fun provideJugadorCache(
        capacity: Long = 5,
        duration: Long = 1000
    ): Cache<Long, Jugador> {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando Caché de Jugadores (capacidad: $capacity - duración: $duration)" }
        return Caffeine.newBuilder()
            .maximumSize(capacity) // LRU con máximo de x elementos
            .expireAfterWrite(duration, TimeUnit.MILLISECONDS) // Expira x milisegundos después de la escritura
            .build<Long, Jugador>()
    }

    /**
     * Provee un servicio, al que se le inyectan el repositorio, la caché, el validador y el storage.
     * @return [JugadorServiceImpl]
     */
    private fun provideJugadorService(
        repository: JugadorRepository,
        validator: JugadorValidator,
        storage: JugadorStorage,
        cache: Cache<Long, Jugador>
    ): JugadorService {
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando Servicio de Jugadores" }
        return JugadorServiceImpl(repository, validator, storage, cache)

    }

    /**
     * Realiza la inyección de dependencias en el orden necesario.
     * @see provideDatabaseManager
     * @see provideJugadorDao
     * @see provideJugadorRepository
     * @see provideJugadorValidator
     * @see provideJugadorStorage
     * @see provideJugadorCache
     * @see provideJugadorService
     */
    fun getJugadoresService(): JugadorService {
        return provideJugadorService(
            repository = provideJugadorRepository(provideJugadorDao(provideDatabaseManager())),
            validator = provideJugadorValidator(),
            storage = provideJugadorStorage(),
            cache = provideJugadorCache()
        )
    }
}