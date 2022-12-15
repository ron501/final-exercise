package com.smg.animequiz.quiz

import com.smg.animequiz.models.Anime
import kotlin.random.Random

class QuestionBank() {

    val questions: ArrayList<Question> = ArrayList()

    fun generateTestQuestions(allAnime: ArrayList<Anime>, count: Int){


        questions.clear()

        val rndList = ArrayList<Int>()
        for (i in 1..count){
            rndList.add(i - 1)
        }
        rndList.forEach{
            questions.add(
                Question(
                    allAnime[it],
                    getRandomAnimes(allAnime[it], 3, allAnime)
                )
            )
        }
    }


    fun generateQuestions(allAnime: ArrayList<Anime>, count: Int){

        questions.clear()

        val rndList = ArrayList<Int>()

        for (i in 1..count){
            var nextInt = Random.nextInt(0, allAnime.count())
            while (rndList.contains(nextInt)){
                nextInt = Random.nextInt(0, allAnime.count())
            }
            rndList.add(nextInt)
        }
        rndList.forEach{
            questions.add(
                Question(
                    allAnime[it],
                    getRandomAnimes(allAnime[it], 3, allAnime)
                )
            )
        }
    }

    private fun getRandomAnimes(anime: Anime, count: Int, sourseList: ArrayList<Anime>): ArrayList<Anime>{
        val animes = ArrayList<Anime>()
        var rndAnime = sourseList[Random.nextInt(0, sourseList.count())]
        for (i in 1..(count + 1)){
            while (rndAnime == anime || animes.contains(rndAnime)){
                rndAnime = sourseList[Random.nextInt(0, sourseList.count())]
            }
            animes.add(rndAnime)
        }
        animes[Random.nextInt(0, animes.count())] = anime
        return animes
    }
}
