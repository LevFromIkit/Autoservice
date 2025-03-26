package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.usersClass
import com.example.mdkp.databinding.FragmentAddClientBinding
import com.example.mdkp.databinding.FragmentReviewsFromUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class addClient : Fragment() {

    lateinit var binding: FragmentAddClientBinding
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.addUserButton.setOnClickListener {
            val name = binding.nameView.text.toString()
            val lastname = binding.lastNameView.text.toString()
            val email = binding.emailView.text.toString()
            val phone = binding.phoneView.text.toString()
            val password = binding.passwordView.text.toString()
            val repPassword = binding.repeatPasswordView.text.toString()

            if (binding.nameView.text.isNullOrBlank())
                binding.nameView.setError(getString(R.string.reg_logError))
            if (binding.lastNameView.text.isNullOrBlank())
                binding.lastNameView.setError(getString(R.string.reg_logError))
            if (binding.phoneView.text.isNullOrBlank())
                binding.phoneView.setError(getString(R.string.reg_logError))
            if (binding.emailView.text.isNullOrBlank())
                binding.emailView.setError(getString(R.string.reg_logError))
            if (binding.passwordView.text.isNullOrBlank())
                binding.passwordView.setError(getString(R.string.reg_logError))
            if (password != repPassword){
                binding.repeatPasswordView.setError(getString(R.string.reg_repeatPasswordError))
                Toast.makeText(requireContext(), getString(R.string.reg_repeatPasswordError), Toast.LENGTH_SHORT).show()
            }else if (name.isNullOrBlank() || lastname.isNullOrBlank() || email.isNullOrBlank() || phone.isNullOrBlank() || password.isNullOrBlank()){
                Toast.makeText(requireContext(), getString(R.string.infillNecc), Toast.LENGTH_SHORT).show()
            }else{

                database = FirebaseDatabase.getInstance().reference
                val id = database.child("user").push().key!!
                val user = usersClass(name, lastname, email, phone, password, id)

                database.child("user").child(id).setValue(user)
                    .addOnSuccessListener{
                        Toast.makeText(requireContext(), getString(R.string.reg_registerSucce), Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), getString(R.string.reg_registerFail), Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}