package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.engineersClass
import classes.serviceClass
import classes.usersClass
import classes.zaivkaClass
import com.example.mdkp.databinding.FragmentZapisViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class zapisView : Fragment() {

    lateinit var binding: FragmentZapisViewBinding
    private val database = FirebaseDatabase.getInstance()
    private val referenceService = database.getReference("service")
    private val referenceEngineer = database.getReference("engineer")
    private val referenceClient = database.getReference("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentZapisViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val data = arguments?.getParcelable<zaivkaClass>("zapis")
        val engineer = arguments?.getParcelable<engineersClass>("engineer")
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("zapis")
        val zapisReference = reference.child(data?.id!!)


        var child = "accept"
        if (engineer != null) {
            zapisReference.child("accept").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val accept = dataSnapshot.getValue(Boolean::class.java)
                    if (accept == true) {
                        binding.delete.text = "завершить"
                        child = "finish"
                    } else {
                        binding.delete.text = "принять"
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }

        binding.delete.setOnClickListener {
            if (engineer != null) {

                if (child == "finish") {
                    val edProbeg = binding.carProbegEd.text.toString()
                    val edCarD = binding.carDescribeEd.text.toString()
                    val edEng = binding.engineerDescribeEd.text.toString()

                    if (edProbeg == "" || edCarD == "" || edEng == "")
                        Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                    else{
                        zapisReference.child(child).setValue(true)
                        zapisReference.child("engineerId").setValue(engineer.id)
                        zapisReference.child("engineerDescription").setValue(edEng)
                        zapisReference.child("carDescription").setValue(edCarD)
                        zapisReference.child("carProbeg").setValue(edProbeg)
                        zapisReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                Toast.makeText(requireContext(), getString(R.string.succesful), Toast.LENGTH_SHORT).show()
                                if (!data.accept)
                                    data.accept = true
                                else
                                    data.finish = true
                                requireActivity().onBackPressed()
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }else{
                if (!data.accept) {
                    zapisReference.removeValue()
                    zapisReference.addListenerForSingleValueEvent(object : ValueEventListener {
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
                }else
                    Toast.makeText(requireContext(), "нельзя удалить, т.к. заявка принята инженером", Toast.LENGTH_LONG).show()
            }
        }

        if (data != null){

            referenceService.orderByChild("id").equalTo(data.serviceId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val service = snapshot.getValue(serviceClass::class.java)
                            binding.nameService.text = getString(R.string.service)+ ": " + service?.name
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        binding.nameService.text = getString(R.string.service)+ ": none"
                    }
                })

            binding.engineerName.text = getString(R.string.engineer)+ ": ещё никто не принял заявку"
            referenceEngineer.orderByChild("id").equalTo(data.engineerId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val engineer = snapshot.getValue(engineersClass::class.java)
                            binding.engineerName.text = getString(R.string.engineer)+ ": " + engineer?.name + " " + engineer?.lastname
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        binding.engineerName.text = getString(R.string.engineer)+ ": none"
                    }
                })

            referenceClient.orderByChild("id").equalTo(data.userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val client = snapshot.getValue(usersClass::class.java)
                            binding.nameClient.text = getString(R.string.client) + ": " + client?.name + " " + client?.lastname
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        binding.engineerName.text = getString(R.string.client) + ": none"
                    }
                })

            binding.clientDescribe.text = "Пожелания клиента: " + data?.clientDescription
            binding.carDescribe.text = "Описание машины: " + data?.carDescription
            binding.engineerDescribe.text = "Комментарий инженера: " + data?.engineerDescription
            binding.data.text = getString(R.string.data) + ": " + data?.hourAndMinute + ", " + data.dayAndMonth
            binding.carProbeg.text = "Пробег авто: " + data?.carProbeg
        }
    }
}