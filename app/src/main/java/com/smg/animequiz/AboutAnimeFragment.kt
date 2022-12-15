package com.smg.animequiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.smg.animequiz.models.AnimeInfo
import com.smg.animequiz.shikimoriapi.ShikimoriService

private const val ARG_PARAM1 = "anime_link"

class AboutAnimeFragment : Fragment() {

    private var animeLink: String? = null

    private lateinit var imageViewPoster: ImageView

    private lateinit var textViewTitle: TextView
    private lateinit var textViewRating: TextView
    private lateinit var textViewYear: TextView
    private lateinit var textViewDescription: TextView

    private var info: AnimeInfo? = null


    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            animeLink = it.getString(ARG_PARAM1)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_anime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.idButtonAddToWatchlist).setOnClickListener {
            buttonAddToWatchlistClick()
        }
        imageViewPoster = view.findViewById(R.id.idImageViewAboutPoster)

        textViewTitle = view.findViewById(R.id.idTextViewTitleName)
        textViewRating = view.findViewById(R.id.idTextViewRating)
        textViewYear = view.findViewById(R.id.idTextViewYear)
        textViewDescription = view.findViewById(R.id.idTextViewDescription)

        buttonBack = view.findViewById<Button?>(R.id.idButtonAboutBack)
        buttonBack.setOnClickListener {
            buttonBackClick()
        }


        QuizApp.instance.getShikimoriService.getAnimeInfo(animeLink!!, QuizApp.instance.applicationContext) {
            receiveAnimeInfo( it )
        }
    }

    private fun buttonBackClick(){
        findNavController().popBackStack()
    }

    private fun receiveAnimeInfo(animeInfo: AnimeInfo){
        info = animeInfo
        QuizApp.instance.getShikimoriService.loadPictureIntoView(imageViewPoster, animeInfo.posterLink)
        textViewTitle.text = animeInfo.title
        textViewRating.text = "Рейтинг: ${animeInfo.rating}"
        textViewYear.text = "Вышло: ${animeInfo.year}"
        val d = if (animeInfo.description.length > 150) (animeInfo.description.substring(0..149) + "...")
        else animeInfo.description
        textViewDescription.text = "Описание:\n$d"
    }

    private fun buttonAddToWatchlistClick(){

        QuizApp.instance.getDBHelper.addTitle(textViewTitle.text.toString(), info!!.fullLink, info!!.smallPosterLink)

    }

}