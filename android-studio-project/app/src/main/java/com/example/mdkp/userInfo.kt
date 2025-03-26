package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.usersClass
import com.example.mdkp.databinding.FragmentUserInfoBinding
import com.google.firebase.database.FirebaseDatabase


lateinit var binding : FragmentUserInfoBinding

class userInfo : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<usersClass>("user")
        if (data != null){

            binding.nameUser.text = getString(R.string.name)+ ": " + data?.name
            binding.lastnameUser.text = getString(R.string.lastName)+ ": " + data?.lastname
            binding.emailUser.text = getString(R.string.email) + ": " + data?.email
            binding.phoneUser.text = getString(R.string.phone) + ": +7" + data?.phone

            if (data?.faild == true)
                binding.vBan.text = getString(R.string.razBan)
            else
                binding.vBan.text = getString(R.string.vBan)
        }

        binding.topAppBar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.addZapis.setOnClickListener {
            val fragment = addZapis()
            val bundle = Bundle()
            bundle.putString("clientId", data?.id)
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.vBan.setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().reference
            val usersId = data?.id
            if (usersId != null) {
                if (data?.faild == false) {
                    databaseReference.child("user").child(usersId).child("faild")
                        .setValue(true).addOnSuccessListener {
                            Toast.makeText(requireContext(), R.string.succesful, Toast.LENGTH_SHORT)
                                .show()
                            requireActivity().onBackPressed()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT)
                                .show()
                        }
                }else{
                    databaseReference.child("user").child(usersId).child("faild")
                        .setValue(false).addOnSuccessListener {
                            Toast.makeText(requireContext(), R.string.succesful, Toast.LENGTH_SHORT)
                                .show()
                            requireActivity().onBackPressed()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }else{
                Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
            }
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
                            requireActivity().onBackPressed()
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