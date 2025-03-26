package com.example.mdkp

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.SmenaClass
import com.example.mdkp.databinding.FragmentSmenEngBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class smenEng : Fragment() {

    private lateinit var smenList: ArrayList<SmenaClass>
    lateinit var binding: FragmentSmenEngBinding
    private val database = FirebaseDatabase.getInstance()
    private val referenceSmen = database.getReference("smens")
    private lateinit var smensAdapter: smenaAdapterForEngineer
    private lateinit var recyclerView: RecyclerView

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
        binding = FragmentSmenEngBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getString(ARG_STRING)

        smenList = ArrayList()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.resView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        smensAdapter = smenaAdapterForEngineer(smenList)
        recyclerView.adapter = smensAdapter

        referenceSmen.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val smena = snapshot.getValue(SmenaClass::class.java)
                    if (smena?.engineerId == data)
                        smenList.add(smena!!)
                }
                smensAdapter.notifyDataSetChanged()
                if (smenList.isEmpty()) {
                    Toast.makeText(requireContext(), getString(com.example.mdkp.R.string.empty), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), getString(com.example.mdkp.R.string.empty), Toast.LENGTH_SHORT).show()
            }
        })
    }
}