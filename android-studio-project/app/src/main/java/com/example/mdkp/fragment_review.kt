package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import classes.reviewClass
import com.example.mdkp.databinding.FragmentReviewsFromUserBinding
import com.example.mdkp.databinding.FragmentReviewBinding

lateinit var bindind: FragmentReviewBinding

class fragment_review : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindind = FragmentReviewBinding.inflate(inflater, container, false)
        return bindind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<reviewClass>("review")
        if (data != null){

            bindind.Title.text = data?.title
            bindind.Author.text = getString(R.string.author)+ ": " + data?.author
            bindind.Mark.text = getString(R.string.mark) + data?.mark.toString()
            bindind.basicText.text = data?.text

        }

        bindind.topAppBar.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }
}