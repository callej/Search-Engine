package search

import java.io.File

const val MENU = "=== Menu ===\n" +
        "1. Find a person\n" +
        "2. Print all people\n" +
        "0. Exit"

fun findPerson(people: List<String> ,pIndex: Map<String, Set<Int>>) {
    println("\nSelect a matching strategy: ALL, ANY, NONE")
    val strategy = readLine()!!.uppercase()
    println("\nEnter a name or email to search all matching people.")
    val query = readLine()!!.lowercase()
    when (strategy) {
        "ALL" -> printResult(people, searchAll(people, pIndex, query))
        "ANY" -> printResult(people, searchAny(people, pIndex, query))
        "NONE" -> printResult(people, searchNone(people, pIndex, query))
        else -> println("\nNo such strategy: $strategy")
    }
}

fun searchAll(people: List<String>, pIndex: Map<String, Set<Int>>, query: String): Set<Int> {
    var result = people.indices.toSet()
    for (q in query.split(" ")) result = result.intersect((pIndex[q] ?: emptySet()).toSet())
    return result
}

fun searchAny(people: List<String>, pIndex: Map<String, Set<Int>>, query: String): Set<Int> {
    var result = emptySet<Int>()
    for (q in query.split(" ")) result = result.union((pIndex[q] ?: emptySet()).toSet())
    return result
}

fun searchNone(people: List<String>, pIndex: Map<String, Set<Int>>, query: String): Set<Int> {
    return people.indices.toSet().subtract(searchAny(people, pIndex, query))
}

fun printResult(people: List<String>, result: Set<Int>) {
    if (result.isNotEmpty()) {
        println("\n${result.size} person${if (result.size > 1) "s" else ""} found:")
        for (index in result) println(people[index])
    } else {
        println("\nNo matching people found.")
    }
}

fun printAll(people: List<String>) {
    println("\n=== List of people ===")
    println(people.joinToString("\n"))
}

fun createIndex(people: List<String>): Map<String, Set<Int>> {
    val peopleIndex = emptyMap<String, Set<Int>>().toMutableMap()
    for (index in people.indices)
        for (entry in people[index].lowercase().split(" "))
            peopleIndex[entry] = peopleIndex[entry]?.plus(index) ?: setOf(index)
    return peopleIndex
}

fun main(args: Array<String>) {
    val filename = args[args.indexOf("--data") + 1]
    val people = File(filename).readLines()
    val peopleIndex = createIndex(people)
    while (true) {
        println("\n$MENU")
        when (readLine()!!) {
            "0" -> break
            "1" -> findPerson(people, peopleIndex)
            "2" -> printAll(people)
            else -> println("\nIncorrect option! Try again.")
        }
    }
    println("\nBye!")
}
