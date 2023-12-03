fun markAdjacent(adjacents: MutableMap<Int, MutableSet<Int>>, x: Int, y: Int) {
    val row = adjacents.getOrPut(y) { mutableSetOf() }
    row.add(x)
    row.add(x - 1)
    row.add(x + 1)
}

fun getAdjacents(input: List<String>): Map<Int, Set<Int>> {
    val adjacents = mutableMapOf<Int, MutableSet<Int>>()
    input.forEachIndexed { x, line ->
        line.forEachIndexed { y, char ->
            if (!char.isDigit() && char != '.') {
                markAdjacent(adjacents, x, y)
                markAdjacent(adjacents, x, y - 1)
                markAdjacent(adjacents, x, y + 1)
            }
        }
    }
    return adjacents
}

fun isAdjacent(adjacents: Map<Int, Set<Int>>, x: Int, y: IntRange): Boolean {
    return y.any {
        adjacents.getOrDefault(it, emptySet()).contains(x)
    }
}

fun findNumbers(numbers: List<List<MatchResult>>, x: Int, y: Int): List<Int> {
    return numbers.getOrNull(y)?.filter {
        it.range.first - 1 <= x && x <= it.range.last + 1
    }?.map { it.value.toInt() } ?: emptyList()
}

fun main() {
    fun part1(input: List<String>): Int {
        val adjacents = getAdjacents(input)
        return input.mapIndexed { index, line ->
            "([0-9]+)".toRegex().findAll(line).sumOf {
                if (isAdjacent(adjacents, index, it.range)) it.value.toInt() else 0
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val numbers = input.map { line ->
            "([0-9]+)".toRegex().findAll(line).toList()
        }
        return input.mapIndexed { y, line ->
            "(\\*)".toRegex().findAll(line).sumOf {
                val x = it.range.first
                val adjacents = findNumbers(numbers, x, y) +
                        findNumbers(numbers, x, y - 1) +
                        findNumbers(numbers, x, y + 1)
                if (adjacents.size == 2) {
                    adjacents[0] * adjacents[1]
                } else {
                    0
                }
            }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
