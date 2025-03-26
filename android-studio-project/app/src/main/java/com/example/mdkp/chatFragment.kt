package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.chatClass
import classes.reviewClass
import com.example.mdkp.databinding.FragmentChatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class chatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: chatAdapter
    private lateinit var chatList: ArrayList<chatClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val ARG_STRING = "string_argument"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getString(reviewsFromUsersFragment.ARG_STRING)
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                //R.id.addUser -> addUser()
                else -> false
            }
            true
        }

        dateInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.reView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        chatAdapter = chatAdapter(chatList)
        recyclerView.adapter = chatAdapter
    }

    private fun dateInitialize(){

        val database = FirebaseDatabase.getInstance()
        val referenceReview = database.getReference("chat")

        chatList = arrayListOf<chatClass>()

        referenceReview.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(chatClass::class.java)
                    chatList.add(chat!!)
                }
                chatAdapter.notifyDataSetChanged() // Обновление адаптера
                if (chatList.isEmpty()) {
                    val chat = chatClass( null, "не", "найдено")
                    chatList.add(chat)
                    chatAdapter.notifyDataSetChanged() // Обновление адаптера
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val chat = chatClass(null, "ошибка", "сервера")
                chatList.add(chat)
                chatAdapter.notifyDataSetChanged() // Обновление адаптера
            }
        })
    }
}