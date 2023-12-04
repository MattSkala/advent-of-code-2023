import kotlin.math.pow

typealias Card = Triple<Int, List<Int>, List<Int>>

fun getWinningNumbers(card: Card): Int {
    val (_, winning, my) = card
    return my.count {
        winning.contains(it)
    }
}

fun parseInput(input: List<String>): List<Card> {
    return input.map { line ->
        val (header, game) = line.split(": ")
        val id = header.replace("Card", "").trim().toInt()
        val (winning, my) = game.split(" | ").map { cards ->
            cards.split(" ").filterNot { it.isBlank() }.map {
                it.toInt()
            }
        }
        Triple(id, winning, my)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val cards = parseInput(input)
        return cards.sumOf {
            val numbers = getWinningNumbers(it)
            if (numbers > 0) {
                2f.pow(getWinningNumbers(it) - 1).toInt()
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val cards = parseInput(input).map {
            Pair(it, 1)
        }.toMutableList()

        cards.forEachIndexed { index, (card, count) ->
            val numbers = getWinningNumbers(card)
            for (i in 1 .. numbers) {
                val newCard = cards[index + i]
                cards[index + i] = Pair(newCard.first, newCard.second + count)
            }
        }

        return cards.sumOf {
            it.second
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
