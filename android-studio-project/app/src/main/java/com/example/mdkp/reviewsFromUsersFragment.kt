package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.reviewClass
import com.example.mdkp.databinding.FragmentReviewsFromUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class reviewsFromUsersFragment : Fragment(R.layout.fragment_review) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewList: ArrayList<reviewClass>
    private lateinit var reviewAdapter: reviewAdapter
    lateinit var bindind: FragmentReviewsFromUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        const val ARG_STRING = "string_argument"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindind = FragmentReviewsFromUserBinding.inflate(inflater, container, false)
        return bindind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindind.createReviewBut.setOnClickListener {
            val data = arguments?.getString(ARG_STRING)
            val fragment = makeReviewFragment()
            val bundle = Bundle()
            bundle.putString("reviews", data)
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        dateInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = bindind.reView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        reviewAdapter = reviewAdapter(reviewList)
        recyclerView.adapter = reviewAdapter

        reviewAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putParcelable("review", it)
            val fragment = fragment_review()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun dateInitialize(){

        val database = FirebaseDatabase.getInstance()
        val referenceReview = database.getReference("review")

        reviewList = arrayListOf<reviewClass>()

        referenceReview.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val review = snapshot.getValue(reviewClass::class.java)
                    reviewList.add(review!!)
                }
                reviewAdapter.notifyDataSetChanged() // Обновление адаптера
                if (reviewList.isEmpty()) {
                    val review = reviewClass( "не", "найдено")
                    reviewList.add(review)
                    reviewAdapter.notifyDataSetChanged() // Обновление адаптера
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val review = reviewClass(null, "ошибка", "сервера")
                reviewList.add(review)
                reviewAdapter.notifyDataSetChanged() // Обновление адаптера
            }
        })
    }
}