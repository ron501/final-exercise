package com.smg.animequiz.quiz
import com.smg.animequiz.models.*

data class Question(
    val answer: Anime,
    val options: ArrayList<Anime>
)