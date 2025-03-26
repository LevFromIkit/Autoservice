package com.example.mdkp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import classes.adminsClass
import classes.reviewClass
import com.example.mdkp.databinding.FragmentMakeReviewBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class makeReviewFragment : Fragment() {

    lateinit var bindind: FragmentMakeReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindind = FragmentMakeReviewBinding.inflate(inflater, container, false)
        return bindind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getString("reviews")
        var author = data
        bindind.author.text = getString(R.string.author) + ": " + author

        var mark = bindind.seekBar.progress

        bindind.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        bindind.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bindind.seekBar.progress
                mark = bindind.seekBar.progress
                bindind.textSeekbar.text = mark.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {     }
        })

        bindind.registerButton.setOnClickListener {
            lateinit var database: DatabaseReference

            val title = bindind.nameView.text.toString()
            val text = bindind.basicView.text.toString()

            val myDate: Date = Calendar.getInstance().time

            if (author == null)
                author = "не определён"

            database = FirebaseDatabase.getInstance().getReference("review")
            val review = reviewClass(title, text, author, mark, myDate)
            database.push().setValue(review).addOnSuccessListener {
                requireActivity().onBackPressed()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), getString(R.string.reg_logError), Toast.LENGTH_SHORT).show()
            }
            requireActivity().onBackPressed()
        }
    }
}