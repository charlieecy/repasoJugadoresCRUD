package org.example.dao

import org.example.models.JugadorEntity
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface JugadorDao {

    /**
     * Ejecuta una consulta que devuelve todos los jugadores de la tabla.
     * @return Una lista de todos los jugadores de la tabla.
     * @see [JugadorEntity]
     */
    @SqlQuery("SELECT * FROM jugadores where isDeleted = false")
    fun getAll(): List<JugadorEntity>

    /**
     * Ejecuta una consulta que devuelve el jugador de la tabla cuyo id coincide con el id buscado, en caso de existir.
     * @return El jugador con el id buscado, o null en caso de no existir.
     * @see [JugadorEntity]
     */
    @SqlQuery("SELECT * FROM jugadores WHERE id = :id AND isDeleted = false")
    fun getById(@Bind("id")id: Long): JugadorEntity?

    /**
     * Inserta un nuevo jugador en la base de datos.
     * @return El id que la base de datos le asigna al nuevo jugador insertado.
     * @see [JugadorEntity]
     */
    @SqlUpdate("INSERT INTO jugadores (nombre, dorsal, posicion, club, createdAt, updatedAt, isDeleted) VALUES (:nombre, :dorsal, :posicion, :club, :createdAt, :updatedAt, :isDeleted)")
    @GetGeneratedKeys("id") //Porque como el id es autonumérico y generado por la BBDD, lo necesitamos, es lo que devuelve la función
    fun save (@BindBean jugador: JugadorEntity): Int

    /**
     * Actualiza un jugador en la base de datos.
     * @return El número de filas de la tabla jugadores actualizadas.
     * @see [JugadorEntity]
     */
    @SqlUpdate("UPDATE jugadores SET nombre = :nombre, dorsal = :dorsal, posicion = :posicion, club = :club, createdAt = :createdAt, updatedAt = :updatedAt, isDeleted= :isDeleted WHERE id = :id")
    fun update(@BindBean jugador: JugadorEntity, @Bind ("id") id: Long): Int

    /**
     * Elimina de la base de datos el jugador con el id que le entra por parámetro, en caso de existir.
     * @return El número de filas de la tabla jugadores afectadas.
     * @see [JugadorEntity]
     */
    @SqlUpdate("UPDATE jugadores SET isDeleted = true WHERE id = :id")
    fun deleteById(@Bind("id")id: Long): Int

    /**
     * Elimina el contenido de la tabla jugadores
     */
    @SqlUpdate("DELETE FROM jugadores")
    fun deleteAll(): Int
}