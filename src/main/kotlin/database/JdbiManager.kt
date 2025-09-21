package org.example.database

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.lighthousegames.logging.logging
/**
 * Clase que representa el JDBI, para simplificar las interacciones con la base de datos.
 */
class JdbiManager {
    private val logger = logging()

    companion object { //al instanciarlo en el companion object, seguimos el patrón singleton, solo habrá una instancia de la clase JdbiManager
        val instance: Jdbi = JdbiManager().jdbi
    }
    val url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    val jdbi = Jdbi.create(url) //se crea la bbdd en base a la url

    init {
        logger.debug { "Inicializando JdbiManager" }
        jdbi.installPlugin(KotlinPlugin())
        jdbi.installPlugin(SqlObjectPlugin())
        executeSqlScriptFromResources("tables.sql")
    }

    /**
     * Ejecuta un script SQL desde la carpeta resources del proyecto.
     */
    fun executeSqlScriptFromResources(resourcePath: String) {
        logger.debug { "JDBI MANAGER: Ejecutando script SQL desde recursos: $resourcePath" }
        val inputStream = ClassLoader.getSystemResourceAsStream(resourcePath)?.bufferedReader()!!
        val script = inputStream.readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }
    }
}