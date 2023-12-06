typealias Game = Pair<Long, Long>

fun main() {
    fun parseNumbers(input: String): List<Long> {
        return input.split(" ")
            .filterIndexed { index, item -> index > 0 && item.isNotBlank() }
            .map { it.trim().toLong() }
    }

    fun parseNumber(input: String): Long {
        return input.replace(" ", "").split(":")[1].toLong()
    }

    fun getWays(game: Game): Int {
        val (time, distance) = game
        return (1 until time).count { buttonTime ->
            val boatTime = time - buttonTime
            val myDistance =  boatTime * buttonTime
            myDistance > distance
        }
    }

    fun solve(games: List<Game>): Int {
        return games.map {
            getWays(it)
        }.reduce { acc, l -> acc * l }
    }

    fun part1(input: List<String>): Int {
        val times = parseNumbers(input[0])
        val distances = parseNumbers(input[1])
        val games = times.mapIndexed { index, time ->
            Pair(time, distances[index])
        }
        return solve(games)
    }

    fun part2(input: List<String>): Int {
        val time = parseNumber(input[0])
        val distance = parseNumber(input[1])
        return getWays(Pair(time, distance))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
