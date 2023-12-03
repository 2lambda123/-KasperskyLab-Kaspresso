package com.kaspresso.components.pageobjectcodegen

import java.lang.StringBuilder

class TextWriter(private val indentation: Int = 0) {

    val line = StringBuilder()
    val lines = mutableListOf<Any>()

    init {
        append(" ".repeat(indentation), 0)
    }

    fun append(text: String, countOfLinesAfterText: Int = 1): TextWriter = apply {
        line.append(text)
        nextLine(countOfLinesAfterText)
    }

    fun nextLine(count: Int = 1): TextWriter = apply {
        for (i in 0 until count) {
            commitLine()
            initNewLine()
        }
    }

    private fun commitLine() {
        lines.add(line.toString())
    }

    private fun initNewLine() {
        line.setLength(0)
        line.append(" ".repeat(indentation))
    }

    fun withIncreasedIndentation(): TextWriter {
        val writer = TextWriter(indentation + INDENTATION_STEP)
        lines.add(writer)
        return writer
    }

    override fun toString(): String {
        commitLine()
        return lines.joinToString("\n")
    }

    fun codeBlock(header: String, countOfLinesAfterBegin: Int = 2, countOfLinesAfterEnd: Int = 1, block: TextWriter.() -> Unit) {
        append("$header {", countOfLinesAfterBegin)
        with(withIncreasedIndentation()) {
            block()
        }
        append("}", countOfLinesAfterEnd)
    }

    companion object Constants {
        private const val INDENTATION_STEP: Int = 4
    }
}
