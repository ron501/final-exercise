package com.smg.animequiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

private const val ARG_PARAM1 = "correct_count"

class CompleteFragment : Fragment() {

    private var correctCount: Int? = null

    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            correctCount = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        textViewResult = view.findViewById(R.id.idTextViewResults)
        textViewResult.text = "Угадано $correctCount/$QUESTION_COUNT"
        //textViewResult.text = "Угадано ${QuizApp.instance.gameState.correctCount}/$QUESTION_COUNT"
        //textViewResult.text = "Угадано ${QuizApp.instance.gameState.correctCount}/$QUESTION_COUNT"

        view.findViewById<Button>(R.id.idButtonCompleteReturn).setOnClickListener {
            buttonReturnClick()
        }
    }

    private fun buttonReturnClick(){
        findNavController().navigate(R.id.action_completeFragment_to_mainFragment)
    }
}