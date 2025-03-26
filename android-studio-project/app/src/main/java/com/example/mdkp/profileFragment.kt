package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.adminsClass
import classes.usersClass
import com.example.mdkp.databinding.FragmentProfileBinding
import com.google.firebase.database.FirebaseDatabase


class profileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<usersClass>("user")
        if (data != null) {
            binding.nameUser.text = getString(R.string.name) + ": " + data?.name
            binding.lastnameUser.text = getString(R.string.lastName) + ": " + data?.lastname
            binding.emailUser.text = getString(R.string.email) + ": " + data?.email
            binding.phoneUser.text = getString(R.string.phone) + ": +7" + data?.phone
        }

        binding.changePasBut.setOnClickListener {
            val newPas = binding.passwordView.text.toString()
            val reNewPas = binding.repeatPasswordView.text.toString()

            if (newPas.isEmpty() or reNewPas.isEmpty()){
                Toast.makeText(requireContext(), R.string.infillNecc, Toast.LENGTH_SHORT).show()
            }else if (newPas != reNewPas){
                Toast.makeText(requireContext(), R.string.reg_repeatPasswordError, Toast.LENGTH_SHORT).show()
            } else {
                val databaseReference = FirebaseDatabase.getInstance().reference
                val userId = data?.id
                if (userId != null) {
                    databaseReference.child("user").child(userId).child("password")
                        .setValue(newPas).addOnSuccessListener {
                            Toast.makeText(requireContext(), R.string.succesful, Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener{
                            Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                        }
                }else{
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}