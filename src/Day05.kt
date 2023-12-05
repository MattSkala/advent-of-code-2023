data class Mapping(val destination: Long, val source: LongRange)

data class ResolveResult(val resolvedRange: LongRange, val remainingRanges: List<LongRange>)

fun parseSeeds(input: MutableList<String>): List<Long> {
    val line = input.removeFirst()
    return line.replace("seeds: ", "").split(" ").map {
        it.toLong()
    }
}

fun parseSeedRanges(input: MutableList<String>): List<LongRange> {
    return parseSeeds(input).chunked(2).map { (start, length) ->
        LongRange(start, start + length - 1)
    }
}

fun parseMapping(input: MutableList<String>): List<Mapping> {
    val mappings = mutableListOf<Mapping>()
    do {
        val line = input.removeFirst()
        if (line.isNotBlank() && line.first().isDigit()) {
            val mapping = line.split(" ").map { it.toLong() }
            val destination = mapping[0]
            val source = LongRange(mapping[1], mapping[1] + mapping[2] - 1)
            mappings.add(Mapping(destination, source))
        }
    } while (input.firstOrNull()?.isNotBlank() == true)
    return mappings
}

fun LongRange.overlaps(other: LongRange): Boolean {
    return this.first <= other.last && other.first <= this.last
}

fun LongRange.length(): Long {
    return this.last - this.first + 1
}

fun resolveMapping(mapping: Mapping, range: LongRange): ResolveResult {
    val (dest, source) = mapping

    val remainingRanges = mutableListOf<LongRange>()
    val resolvedRange: LongRange

    if (source.first <= range.first && source.last <= range.last) {
        resolvedRange = LongRange(
            dest + range.first - source.first,
            dest + source.last - source.first
        )
        remainingRanges += LongRange(source.last + 1, range.last)
    } else if (source.first >= range.first && source.last >= range.last) {
        resolvedRange = LongRange(
            dest,
            dest + source.length() - (source.last - range.last) - 1
        )
        remainingRanges += LongRange(range.first, source.first - 1)
    } else if (source.first <= range.first /* && source.last >= range.last */) {
        resolvedRange = LongRange(
            dest + range.first - source.first,
            dest + (range.first - source.first) + range.length() - 1
        )
    } else /* if (source.first >= range.first && source.last <= range.last) */ {
        resolvedRange = LongRange(
            dest,
            dest + source.length() - 1
        )
        remainingRanges += LongRange(range.first, source.first - 1)
        remainingRanges += LongRange(source.last + 1, range.last)
    }

    return ResolveResult(resolvedRange, remainingRanges.filter { !it.isEmpty() })
}

fun resolveMapping(mappings: List<Mapping>, values: List<LongRange>): List<LongRange> {
    var remainingRanges = values
    val resolvedRanges = mutableListOf<LongRange>()

    do {
        var hasAnyOverlaps = false
        val newRemainingRanges = mutableListOf<LongRange>()
        remainingRanges.forEach remainingRanges@ { range ->
            mappings.forEach { mapping ->
                if (range.overlaps(mapping.source)) {
                    val (resolvedRange, sourceRemainingRanges) = resolveMapping(mapping, range)
                    resolvedRanges += resolvedRange
                    newRemainingRanges += sourceRemainingRanges
                    hasAnyOverlaps = true
                    return@remainingRanges
                }
            }
            newRemainingRanges += range
        }
        remainingRanges = newRemainingRanges
    } while (hasAnyOverlaps)

    return (resolvedRanges + remainingRanges).sortedBy { it.first }
}

fun resolveMinLocation(seeds: List<LongRange>, mutableInput: MutableList<String>): Long {
    val seedToSoil = parseMapping(mutableInput)
    val soilToFertilizer = parseMapping(mutableInput)
    val fertilizerToWater = parseMapping(mutableInput)
    val waterToLight = parseMapping(mutableInput)
    val lightToTemperature = parseMapping(mutableInput)
    val temperatureToHumidity = parseMapping(mutableInput)
    val humidityToLocation = parseMapping(mutableInput)
    val soil = resolveMapping(seedToSoil, seeds)
    val fertilizer = resolveMapping(soilToFertilizer, soil)
    val water = resolveMapping(fertilizerToWater, fertilizer)
    val light = resolveMapping(waterToLight, water)
    val temperature = resolveMapping(lightToTemperature, light)
    val humidity = resolveMapping(temperatureToHumidity, temperature)
    val location = resolveMapping(humidityToLocation, humidity)
    return location.minOf { it.first }
}

fun main() {
    fun part1(input: List<String>): Long {
        val mutableInput = input.toMutableList()
        val seeds = parseSeeds(mutableInput).map { LongRange(it, it) }
        return resolveMinLocation(seeds, mutableInput)
    }

    fun part2(input: List<String>): Long {
        val mutableInput = input.toMutableList()
        val seeds = parseSeedRanges(mutableInput)
        return resolveMinLocation(seeds, mutableInput)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
