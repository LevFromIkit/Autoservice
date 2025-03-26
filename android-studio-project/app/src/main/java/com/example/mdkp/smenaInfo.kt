package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.SmenaClass
import classes.engineersClass
import com.example.mdkp.databinding.FragmentSmenaInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class smenaInfo : Fragment() {

    lateinit var binding: FragmentSmenaInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSmenaInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<SmenaClass>("smena")
        if (data != null){
            if (data.engineerId != null) {

                val databaseReference = FirebaseDatabase.getInstance().getReference("engineer")
                databaseReference.child(data.engineerId).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val engineer = dataSnapshot.getValue(engineersClass::class.java)
                            if (engineer != null)
                                binding.name.text = getString(R.string.name)+ ": " + engineer.name + " " + engineer.lastname
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

            binding.date.text = String.format("%s: %02d.%02d.%04d", getString(R.string.data), data.day, data.mounth, data.year)
            binding.timeFrom.text = String.format("%s: %02d:%02d", getString(R.string.timeFrom), data.hourStart, data.minuteStart)
            binding.timeTo.text = String.format("%s: %02d:%02d", getString(R.string.timeTo), data.hourEnd, data.minuteEnd)
        }

        binding.topAppBar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.delete.setOnClickListener {
            val calendar = Calendar.getInstance()
            val curDate = Date()
            calendar.time = curDate

            calendar.set(Calendar.YEAR, data?.year!!)
            calendar.set(Calendar.MONTH, data?.mounth!! - 1)
            calendar.set(Calendar.DAY_OF_MONTH, data?.day !!)

            val smenDate: Date = calendar.time

            if (curDate.before(smenDate)) {
                val database = FirebaseDatabase.getInstance().getReference("smens")
                data?.id?.let { it1 ->
                    database.child(it1).removeValue().addOnSuccessListener {
                        requireActivity().onBackPressed()
                        Toast.makeText(view.context, R.string.succesful, Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { error ->
                        Toast.makeText(view.context, R.string.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }else {
                Toast.makeText(view.context, "Нельзя удалить, т.к. смена уже прошла", Toast.LENGTH_SHORT).show()
            }
        }

    }
}