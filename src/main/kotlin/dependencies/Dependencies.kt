package org.example.dependencies

import org.example.dao.JugadorDao
import org.example.database.JdbiManager
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging


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
        logger.debug { "INYECCIÓN DEPENDENCIAS: Proporcionando DAO de Integrantes" }
        return jdbi.onDemand(JugadorDao::class.java)
    }
}