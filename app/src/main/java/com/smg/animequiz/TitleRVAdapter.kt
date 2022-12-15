package com.smg.animequiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.smg.animequiz.models.TitleRVModel

class TitleRVAdapter (
    private var titlesList: ArrayList<TitleRVModel>,
): RecyclerView.Adapter<TitleRVAdapter.TitleViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TitleRVAdapter.TitleViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.watchlist_rv_item,
            parent, false
        )

        return TitleViewHolder(itemView)

    }

    //fun filterList(filterList: ArrayList<>){
    //    titlesList = filterList
    //    notifyDataSetChanged()
    //}

    override fun onBindViewHolder(holder: TitleRVAdapter.TitleViewHolder, position: Int) {
        holder.titleNameTV.text = titlesList[position].name
        holder.link = titlesList[position].link
        holder.posterLink = titlesList[position].posterLink
        //QuizApp.instance.getShikimoriService.loadPictureIntoView(
        //    holder.titleIV,
        //    titlesList[position].posterLink)
    }

    override fun onViewAttachedToWindow(holder: TitleViewHolder) {
        super.onViewAttachedToWindow(holder)
        QuizApp.instance.getShikimoriService.loadPictureIntoView(
            holder.titleIV,
            holder.posterLink)

    }

    override fun getItemCount(): Int {
        return titlesList.count()
    }

    class TitleViewHolder(itemView: View, ): RecyclerView.ViewHolder(itemView){
        val titleNameTV: TextView = itemView.findViewById(R.id.idTVTitle)
        val titleIV: ImageView = itemView.findViewById(R.id.idIVAnimeIcon)
        public var link: String = ""
        public var posterLink: String =""
        init{
            itemView.findViewById<CardView>(R.id.idTitleItemCardView).setOnClickListener{
                val b = Bundle()
                b.putString("anime_link", link)
                val quizFragment = MainActivity.getInstance!!
                    .findViewById<FragmentContainerView>(R.id.fragmentContainerView)
                    .getFragment<WatchlistFragment>()
                findNavController(quizFragment.requireView()).navigate(R.id.action_watchlistFragment_to_aboutAnimeFragment, b)
            }
        }
    }

    private fun cardViewClick(link: String){

    }
}
