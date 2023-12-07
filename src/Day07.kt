enum class HandType {
    HighCard,
    OnePair,
    TwoPairs,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind
}

fun main() {
    val joker = 1

    fun getType(cards: List<Int>): HandType {
        val jokers = cards.count { it == joker }
        val nonJokers = cards.filterNot { it == joker }
        val counts = nonJokers.fold(mutableMapOf<Int, Int>()) { acc, card ->
            acc[card] = acc.getOrDefault(card, 0) + 1
            acc
        }
        val maxCount = (counts.maxOfOrNull { it.value } ?: 0) + jokers
        return when {
            maxCount == 5 -> HandType.FiveOfAKind
            maxCount == 4 -> HandType.FourOfAKind
            maxCount == 3 && counts.size == 2 -> HandType.FullHouse
            maxCount == 3 -> HandType.ThreeOfAKind
            maxCount == 2 && counts.size == 4 -> HandType.OnePair
            maxCount == 1 -> HandType.HighCard
            else -> HandType.TwoPairs
        }
    }

    data class Hand(val cards: List<Int>, val bid: Int)

    fun parseHand(input: String, strengths: Map<Char, Int>): Hand {
        val (cards, bid) = input.split(" ")
        val cardStrengths = cards.map {
            if (it.isDigit()) {
                it.digitToInt()
            } else {
                strengths[it]!!
            }
        }
        return Hand(cardStrengths, bid.toInt())
    }

    fun compareHands(a: List<Int>, b: List<Int>): Int {
        a.forEachIndexed { index, value ->
            if (value > b[index]) {
                return 1
            } else if (value < b[index]) {
                return -1
            }
        }
        return 0
    }

    fun solve(hands: List<Hand>): Long {
        return hands.sortedWith { a, b ->
            val typeA = getType(a.cards)
            val typeB = getType(b.cards)
            if (typeA == typeB) {
                val compared = compareHands(a.cards, b.cards)
                compared
            } else {
                typeA.compareTo(typeB)
            }
        }.mapIndexed { index, hand ->
            hand.bid.toLong() * (index + 1)
        }.sum()
    }

    fun part1(input: List<String>): Long {
        val strengths = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'J' to 11,
            'T' to 10,
        )
        return solve(input.map {
            parseHand(it, strengths)
        })
    }

    fun part2(input: List<String>): Long {
        val strengths = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'J' to joker,
            'T' to 10,
        )
        return solve(input.map {
            parseHand(it, strengths)
        })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
