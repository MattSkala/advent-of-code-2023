import java.awt.Color

enum class CubeColor {
    red,
    green,
    blue
}

fun parseGame(line: String): Pair<Int, List<Map<CubeColor, Int>>> {
    val (prefix, input) = line.split(": ")
    val draws = input
        .split("; ")
        .map { draw ->
            val colors = draw.split(", ")
            colors.associate {
                val (count, color) = it.split(" ")
                CubeColor.valueOf(color) to count.toInt()
            }
        }
    val id = prefix.replace("Game ", "").toInt()
    return id to draws
}

fun isPossible(draw: Map<CubeColor, Int>, bag: Map<CubeColor, Int>): Boolean {
    return draw.all { (color, count) ->
        bag.getOrDefault(color, 0) >= count
    }
}

fun getMinCubes(game: List<Map<CubeColor, Int>>): Map<CubeColor, Int> {
    val minCubes = mutableMapOf<CubeColor, Int>()
    game.forEach { draw ->
        draw.forEach { (color, count) ->
            val currentCount = minCubes.getOrDefault(color, 0)
            if (currentCount < count) {
                minCubes[color] = count
            }
        }
    }
    return minCubes
}

fun main() {
    fun part1(input: List<String>): Int {
        val bag = mapOf(
            CubeColor.red to 12,
            CubeColor.green to 13,
            CubeColor.blue to 14,
        )
        return input.sumOf { line ->
            val (id, game) = parseGame(line)
            val isGamePossible = game.all { draw ->
                isPossible(draw, bag)
            }
            if (isGamePossible) id else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val (_, game) = parseGame(line)
            val minCubes = getMinCubes(game)
            minCubes.getOrDefault(CubeColor.red, 1) *
                    minCubes.getOrDefault(CubeColor.green, 1) *
                    minCubes.getOrDefault(CubeColor.blue, 1)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
