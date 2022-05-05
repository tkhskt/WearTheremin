package com.tkhskt.theremin.core

import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.roundToInt

object NoteMapper {
    private val notes = listOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")

    fun mapFromFrequency(frequency: Float): NoteInfo {
        val midiKeyNumber = (12 * log2(frequency / 440) + 69).roundToInt()
        val noteIndex = (midiKeyNumber - 21) % notes.size
        val noteFrequency = 2.0.pow((midiKeyNumber - 69.0) / 12.0) * 440
        val cent = 1200 * log2(frequency.roundToInt() / noteFrequency)
        return NoteInfo(
            note = notes[noteIndex],
            midiKeyNumber = midiKeyNumber,
            centDiff = cent.toInt(),
        )
    }
}
