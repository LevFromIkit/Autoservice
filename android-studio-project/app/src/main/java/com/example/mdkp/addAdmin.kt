package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.adminsClass
import classes.engineersClass
import com.example.mdkp.databinding.FragmentAddAdminBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class addAdmin : Fragment() {

    lateinit var bindin: FragmentAddAdminBinding
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindin = FragmentAddAdminBinding.inflate(inflater, container, false)
        return bindin.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindin.cancelButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        bindin.addUserButton.setOnClickListener {
            val name = bindin.nameView.text.toString()
            val lastname = bindin.lastNameView.text.toString()
            val email = bindin.emailView.text.toString()
            val phone = bindin.phoneView.text.toString()
            val login = bindin.loginView.text.toString()
            val password = bindin.passwordView.text.toString()
            val repPassword = bindin.repeatPasswordView.text.toString()

            if (bindin.nameView.text.isNullOrBlank())
                bindin.nameView.setError(getString(R.string.reg_logError))
            if (bindin.lastNameView.text.isNullOrBlank())
                bindin.lastNameView.setError(getString(R.string.reg_logError))
            if (bindin.phoneView.text.isNullOrBlank())
                bindin.phoneView.setError(getString(R.string.reg_logError))
            if (bindin.emailView.text.isNullOrBlank())
                bindin.emailView.setError(getString(R.string.reg_logError))
            if (bindin.loginView.text.isNullOrBlank())
                bindin.loginView.setError(getString(R.string.reg_logError))
            if (bindin.passwordView.text.isNullOrBlank())
                bindin.passwordView.setError(getString(R.string.reg_logError))
            if (password != repPassword){
                bindin.repeatPasswordView.setError(getString(R.string.reg_repeatPasswordError))
                Toast.makeText(requireContext(), getString(R.string.reg_repeatPasswordError), Toast.LENGTH_SHORT).show()
            }else if (name.isNullOrBlank() || lastname.isNullOrBlank() || email.isNullOrBlank() || phone.isNullOrBlank() || password.isNullOrBlank() || login.isNullOrBlank()){
                Toast.makeText(requireContext(), getString(R.string.infillNecc), Toast.LENGTH_SHORT).show()
            }else{

                database = FirebaseDatabase.getInstance().reference
                database.child("admin").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var findUser = false
                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(adminsClass::class.java)

                            if (user?.login == login) {
                                findUser = true
                            }
                        }
                        if (findUser)
                            Toast.makeText(requireContext(),"Такой номер логин уже существует", Toast.LENGTH_SHORT).show()
                        else{
                            val id = database.child("admin").push().key!!
                            val admin = adminsClass("admin" + login, name, lastname, email, phone, password, id)
                            database.child("admin").child(id).setValue(admin)
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