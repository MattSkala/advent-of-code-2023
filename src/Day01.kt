fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {  line ->
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }
            (first.toString() + last).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val numbers = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )
        return input.sumOf {  line ->
            var newLine = line

            val firstDigitIndex = newLine.indexOfFirst { it.isDigit() }
            val firstNumber = numbers.map { (key) ->
                key to key.toRegex().find(newLine)?.range
            }.filter { it.second != null }.minByOrNull { it.second?.start!! }

            if (firstNumber != null) {
                val number = firstNumber.first
                val range = firstNumber.second!!
                if (firstDigitIndex == -1 || range.first < firstDigitIndex) {
                    newLine = newLine.replaceRange(range, numbers[number].toString())
                }
            }

            val lastDigitIndex = newLine.indexOfLast { it.isDigit() }
            val lastNumber = numbers.map { (key) ->
                key to key.toRegex().findAll(newLine).lastOrNull()?.range
            }.filter { it.second != null }.maxByOrNull { it.second?.start!! }

            if (lastNumber != null) {
                val number = lastNumber.first
                val range = lastNumber.second!!
                if (lastDigitIndex == -1 || range.first > lastDigitIndex) {
                    newLine = newLine.replaceRange(range, numbers[number].toString())
                }
            }

            val first = newLine.first { it.isDigit() }
            val last = newLine.last { it.isDigit() }
            (first.toString() + last).toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test_part2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
