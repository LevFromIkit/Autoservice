package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.serviceClass
import com.example.mdkp.databinding.FragmentServiceInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class serviceInfo : Fragment() {

    lateinit var binding: FragmentServiceInfoBinding
    private val database = FirebaseDatabase.getInstance()
    private val referenceService = database.getReference("service")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<serviceClass>("service")

        binding.topAppBar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.delete.setOnClickListener {

            referenceService.child(data?.id!!).removeValue()
            referenceService.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.succesful),
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().onBackPressed()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        if (data != null){

            binding.name.text = "Наименование: " + data?.name
            binding.duration.text = "Продолжительность работ: " + data?.duration.toString()
            binding.speciality.text = "Вид работ: " + data?.speciliality
            binding.cost.text = "Стоимость: " + data?.cost.toString()
        }
    }
}