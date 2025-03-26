package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import classes.serviceClass
import com.example.mdkp.databinding.FragmentServiceCreateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class serviceCreate : Fragment() {

    lateinit var binding: FragmentServiceCreateBinding
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = arrayOf("двигатель", "трансмиссия", "подвеска", "электрика", "плановое ТО", "диагностика", "другое")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.topAppBar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.create.setOnClickListener{
            val speciality = binding.spinner.selectedItemPosition
            val name = binding.name.text.toString()
            val duration = binding.duration.text.toString().toInt()
            val cost = binding.cost.text.toString().toInt()

            database = FirebaseDatabase.getInstance().reference
            val id = database.child("service").push().key!!

            val service = serviceClass(id, name, duration, items[speciality], cost)
            database.child("service").child(id).setValue(service)
                .addOnSuccessListener{
                    Toast.makeText(requireContext(), getString(R.string.succesful), Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
        }
    }
}