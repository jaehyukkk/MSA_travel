package com.example.travel_search.util

import org.springframework.stereotype.Component

@Component
class CombinationsGenerator {

    fun <T> generateCombinations(elements: List<T>, size: Int): List<List<T>> {
        val result = mutableListOf<List<T>>()
        generateCombinationsRecursive(elements, size, 0, mutableListOf(), result)
        return result
    }

    private fun <T> generateCombinationsRecursive(
        elements: List<T>,
        size: Int,
        startIndex: Int,
        currentCombination: MutableList<T>,
        result: MutableList<List<T>>
    ) {
        if (currentCombination.size == size) {
            result.add(ArrayList(currentCombination))
            return
        }
        for (i in startIndex until elements.size) {
            currentCombination.add(elements[i])
            generateCombinationsRecursive(elements, size, i + 1, currentCombination, result)
            currentCombination.removeAt(currentCombination.size - 1)
        }
    }
}
