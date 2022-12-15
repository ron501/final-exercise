package com.smg.animequiz.shikimoriapi
import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.smg.animequiz.LOG_TAG
import com.smg.animequiz.MainActivity
import com.smg.animequiz.TestResponses
import com.smg.animequiz.models.Anime
import com.smg.animequiz.models.AnimeInfo
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

class ShikimoriService {

    private var jsonContents: String? = null

    val jsonContent get() = jsonContents

    val allAnimeTitles: ArrayList<Anime> = ArrayList()

    val picasso = Picasso.get()


    fun interface DataParsedCallback{
        fun run(success: Boolean)
    }

    public fun getTestMainJsonString(count: Int,year: Int, queue: RequestQueue, callback: DataParsedCallback){

        jsonContents = when(year){
            1990 -> TestResponses.response1990
            2000 -> TestResponses.response2000
            2010 -> TestResponses.response2010
            2018 -> TestResponses.response2018
            else -> { TestResponses.response2018 }
        }
        callback.run(this.parseJsonData())
    }


    public fun getMainJsonString(count: Int,year: Int, queue: RequestQueue, callback: DataParsedCallback){

        val yearParam = when(year){
            1990 -> "1990_1999"
            2000 -> "2000_2009"
            2010 -> "2010_2017"
            2018 -> "2018_2022"
            else -> { "2018_2022" }
        }

        Log.d(LOG_TAG, "Getting main json string, year: $yearParam")

        //val queue = Volley.newRequestQueue(context)
        //queue = Volley.newRequestQueue(context)
        val totalCount = count + 20

        val link = "https://shikimori.one/api/animes?kind=tv,movie&score=8&order=random&limit=$totalCount&season=$yearParam"

        Log.d(LOG_TAG, "LINKl: $link")


        val stringRequest = object: StringRequest(
            Request.Method.GET, link,
            Response.Listener<String> { response ->
                jsonContents = response
                Log.d(LOG_TAG, "GOT RESPONSE")
                callback.run(this.parseJsonData())
            },
            Response.ErrorListener {
                Log.d(LOG_TAG, "GOT ERROR ${it.message}")
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "node-shikimori"
                return headers
            }
        }
        queue?.add(stringRequest)
        Log.d(LOG_TAG, "ADDED REQUEST")
    }

    public fun parseJsonData(): Boolean{
        if (jsonContent == null){
            Log.e("QUIZ_ERROR", "ERROR: jsonContent is empty")
            return false
        }

        val content = JSONArray(jsonContent)
        if (content.length() == 0){
            Log.e("QUIZ_ERROR", "ERROR: jsonContent is not valid")
            return false
        }

        for (i in 0 until content.length()){
            val anime = content.getJSONObject(i)
            val title = anime.getString("russian")
            val link = anime.getString("url")
            allAnimeTitles.add(Anime(title, link))
        }
        return true
    }

    private class ScreenShotLoader(val totalCount: Int){
        var count: Int = 0
        fun load(): Boolean {
            count++
            return count >= totalCount
        }
    }
    private var screenShotLoader: ScreenShotLoader? = null

    public fun loadPictureIntoView(view: ImageView, link: String){
        Log.d(LOG_TAG, "https://shikimori.one$link")
        picasso.load("https://shikimori.one$link").into(view)
    }


    fun interface ReceiveAnimeInfo{
        fun receive(animeInfo: AnimeInfo)
    }



    public fun getAnimeInfo(animeLink: String, context: Context, receiver: ReceiveAnimeInfo ){
        val queue = Volley.newRequestQueue(context)
        val link = "https://shikimori.one/api$animeLink"
        val stringRequest = object: StringRequest(
            Request.Method.GET, link,
            Response.Listener<String> { response ->

                val obj = JSONObject(response)
                val title = obj.getString("russian")
                val kind = obj.getString("kind")
                val year = obj.getString(
                    if(kind == "movie") "released_on" else "aired_on")
                val rating = obj.getString("score")
                val description = obj.getString("description")
                val image = obj.getJSONObject("image")
                val posterLink = image.getString("original")
                val smallPosterLink = image.getString("x96")

                val info = AnimeInfo(
                    link,
                    title,
                    year,
                    rating,
                    description,
                    posterLink,
                    smallPosterLink
                )
                receiver.receive(info)
            },
            Response.ErrorListener {  })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "node-shikimori"
                return headers
            }
        }
        queue.add(stringRequest)
    }

    public fun getAnimeScreenshotLinksTest(animes: ArrayList<Anime>, queue: RequestQueue, callback: DataParsedCallback) {
        Log.d(LOG_TAG, "GETTING SCREENSHOTS")
        screenShotLoader = ScreenShotLoader(animes.count())
        for (i in 0..9){
            val linkArray = JSONObject(TestResponses.animeResponses2018[i]).getJSONArray("screenshots")
            if (linkArray.length() != 0){
                animes[i].screenshotLink = linkArray.getJSONObject(0).getString("preview")
                //animes[i].screenshotLink = linkArray.getString(Random.nextInt(0, linkArray.length()))
                if (screenShotLoader!!.load()){
                    screenShotLoader = null
                }
            }
        }
        callback.run(true)
    }


    public fun getAnimeScreenshotLinks(animes: ArrayList<Anime>, queue: RequestQueue, callback: DataParsedCallback) {
        screenShotLoader = ScreenShotLoader(animes.count())
        for (anime in animes){
            val link = "https://shikimori.one/api${anime.link}"
            val stringRequest = object: StringRequest(
                Request.Method.GET, link,
                Response.Listener<String> { response ->
                    val linkArray = JSONObject(response).getJSONArray("screenshots")
                    if (linkArray.length() == 0){
                        Log.e("QUIZ_ERROR", "Error while trying to get screenshot link.")
                    } else {
                        anime.screenshotLink = linkArray.getString(Random.nextInt(0, linkArray.length()))
                        if (screenShotLoader!!.load()){
                            screenShotLoader = null
                            callback.run(true)
                        }
                    }
                },
                Response.ErrorListener {  })
            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "node-shikimori"
                    return headers
                }
            }
            queue.add(stringRequest)
        }
    }
}