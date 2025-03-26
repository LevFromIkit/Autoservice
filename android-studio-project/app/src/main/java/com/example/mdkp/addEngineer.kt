package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.adminsClass
import classes.engineersClass
import classes.usersClass
import com.example.mdkp.databinding.FragmentAddEngineerBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class addEngineer : Fragment() {

    lateinit var bindin: FragmentAddEngineerBinding
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindin = FragmentAddEngineerBinding.inflate(inflater, container, false)
        return bindin.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().reference

        bindin.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        bindin.addUserButton.setOnClickListener {
            val name = bindin.nameView.text.toString()
            val lastname = bindin.lastNameView.text.toString()
            val email = bindin.emailView.text.toString()
            val phone = bindin.phoneView.text.toString()
            val login = bindin.loginView.text.toString()
            val speciality = bindin.specialityView.text.toString()
            val password = bindin.passwordView.text.toString()
            val repPassword = bindin.repeatPasswordView.text.toString()

            if (bindin.nameView.text.isNullOrBlank())
                bindin.nameView.setError(getString(R.string.reg_logError))
            if (bindin.lastNameView.text.isNullOrBlank())
                bindin.lastNameView.setError(getString(R.string.reg_logError))
            if (bindin.specialityView.text.isNullOrBlank())
                bindin.specialityView.setError(getString(R.string.reg_logError))
            if (bindin.phoneView.text.isNullOrBlank())
                bindin.phoneView.setError(getString(R.string.reg_logError))
            if (bindin.emailView.text.isNullOrBlank())
                bindin.emailView.setError(getString(R.string.reg_logError))
            if (bindin.loginView.text.isNullOrBlank())
                bindin.loginView.setError(getString(R.string.reg_logError))
            if (bindin.passwordView.text.isNullOrBlank())
                bindin.passwordView.setError(getString(R.string.reg_logError))
            if ("admin" in login)
                bindin.loginView.setError(getString(R.string.loginEngineerError))
            else if (password != repPassword){
                bindin.repeatPasswordView.setError(getString(R.string.reg_repeatPasswordError))
                Toast.makeText(requireContext(), getString(R.string.reg_repeatPasswordError), Toast.LENGTH_SHORT).show()
            }else if (name.isNullOrBlank() || lastname.isNullOrBlank() || email.isNullOrBlank() || phone.isNullOrBlank() || password.isNullOrBlank() || login.isNullOrBlank() || login.isNullOrBlank() ){
                Toast.makeText(requireContext(), getString(R.string.infillNecc), Toast.LENGTH_SHORT).show()
            }else{
                database = FirebaseDatabase.getInstance().reference
                database.child("engineer").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var findUser = false
                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(engineersClass::class.java)

                            if (user?.login == login) {
                                findUser = true
                            }
                        }
                        if (findUser)
                            Toast.makeText(requireContext(),"Такой номер логин уже существует", Toast.LENGTH_SHORT).show()
                        else{
                            val id = database.child("engineer").push().key!!
                            val engineer = engineersClass(login, name, lastname, speciality, email, phone, password, id)
                            database.child("engineer").child(id).setValue(engineer)
                                .addOnSuccessListener{
                                    Toast.makeText(requireContext(), getString(R.string.reg_registerSucce), Toast.LENGTH_SHORT).show()
                                    requireActivity().onBackPressed()
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(), getString(R.string.reg_registerFail), Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(requireContext(), getString(R.string.serverError), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}