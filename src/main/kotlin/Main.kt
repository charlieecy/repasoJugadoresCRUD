package org.example

import org.example.dependencies.Dependencies
import java.io.File

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val service = Dependencies.getJugadoresService()
    val file = File("data/data.csv")
    service.importFromFile(file.toPath())

    println("CONSULTAS:")
    println("-------------------------------")
    println("Filtrar jugadores por club (Real Madrid)")
    println(service.getAll().filter { it.club == "Real Madrid" })
    println("-------------------------------")
    println("Agrupar por posición")
    println(service.getAll().groupBy { it.posicion })
    println("-------------------------------")
    println("Por cada club, cuántos jugadores hay por posición")
   println(service.getAll()
        .groupBy { it.club }  // agrupa por club
        .mapValues { (_, jugadoresPorClub) ->
            jugadoresPorClub.groupingBy { it.posicion }.eachCount() // agrupa por posición y cuenta
        })

}