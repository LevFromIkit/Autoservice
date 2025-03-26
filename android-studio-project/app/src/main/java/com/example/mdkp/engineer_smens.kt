package com.example.mdkp

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import classes.SmenaClass
import classes.engineersClass
import classes.zaivkaClass
import com.example.mdkp.databinding.FragmentEngineerSmensBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class engineer_smens : Fragment() {
    lateinit var binding: FragmentEngineerSmensBinding
    lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEngineerSmensBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timePicker = binding.timePickerFor
        timePicker.setIs24HourView(true)
        timePicker.minute = 0
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            if (minute % 15 != 0) {
                val roundedMinute = (minute / 15) * 15
                timePicker.minute = roundedMinute
                Toast.makeText(
                    requireContext(),
                    "Выберите время, кратное 15 минутам.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

            val timePicker2 = binding.timePickerFrom
            timePicker2.setIs24HourView(true)
            timePicker2.minute = 0
            timePicker2.setOnTimeChangedListener { _, hourOfDay, minute ->
                if (minute % 15 != 0) {
                    val roundedMinute = (minute / 15) * 15
                    timePicker2.minute = roundedMinute
                    Toast.makeText(
                        requireContext(),
                        "Выберите время, кратное 15 минутам.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        binding.topAppBarr.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val data = arguments?.getParcelable<engineersClass>("engineer")
        binding.topAppBarr.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.example.mdkp.R.id.complete -> {
                    val timePicker = binding.timePickerFrom
                    val hourStart = timePicker.hour
                    val minuteStart = timePicker.minute

                    val timePicker2 = binding.timePickerFor
                    val hourEnd = timePicker2.hour
                    val minuteEnd = timePicker2.minute

                    val datePicker = binding.calendarView

                    val year = datePicker.year
                    val month = datePicker.month
                    val day = datePicker.dayOfMonth

                    val setka = false

                    database = FirebaseDatabase.getInstance().reference
                    val id = database.child("smens").push().key!!
                    val user = SmenaClass(data?.id, day, month, year, hourStart, minuteStart, hourEnd, minuteEnd, setka, id)

                    database.child("smens").child(id).setValue(user)
                        .addOnSuccessListener{
                            Toast.makeText(requireContext(), "успешно", Toast.LENGTH_SHORT).show()
                            requireActivity().onBackPressed()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "ошибка", Toast.LENGTH_SHORT).show()
                        }
                }
                else -> false
            }
            true
        }

    }
}