package com.example.mdkp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import classes.usersClass
import com.example.mdkp.databinding.ActivityRegistratingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class registrating : AppCompatActivity() {

    lateinit var binding: ActivityRegistratingBinding
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrating)

        binding = ActivityRegistratingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener{
            val name = binding.nameView.text.toString()
            val lastname = binding.lastNameView.text.toString()
            val email = binding.emailView.text.toString()
            val phone = binding.phoneView.text.toString()
            val password = binding.passwordView.text.toString()
            val repPassword = binding.repeatPasswordView.text.toString()

            if ((name != "") && (lastname != "") && (email != "") && (phone != "") && (password != "")){
                if (password == repPassword) {
                    database = FirebaseDatabase.getInstance().reference
                    database.child("user").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var findUser = false
                            for (snapshot in dataSnapshot.children) {
                                val user = snapshot.getValue(usersClass::class.java)

                                if (user?.email == email || user?.phone == phone) {
                                    findUser = true
                                }
                            }
                            if (findUser)
                                Toast.makeText(this@registrating,"Такой номер телефона или e-mail уже существует", Toast.LENGTH_SHORT).show()
                            else{
                                val id = database.child("user").push().key!!
                                val user = usersClass(name, lastname, email, phone, password, id)
                                database.child("user").child(id).setValue(user)
                                    .addOnSuccessListener{
                                        Toast.makeText(this@registrating, getString(R.string.reg_registerSucce), Toast.LENGTH_SHORT).show()
                                        finish()
                                    }.addOnFailureListener {
                                        Toast.makeText(this@registrating, getString(R.string.reg_registerFail), Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(this@registrating, getString(R.string.serverError), Toast.LENGTH_SHORT).show()
                        }
                    })
                } else{
                    binding.repeatPasswordView.setError(getString(R.string.reg_repeatPasswordError))
                    Toast.makeText(
                        this,
                        getString(R.string.reg_repeatPasswordError),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(this@registrating, getString(R.string.reg_logInvalid), Toast.LENGTH_SHORT).show()
            }
        }

    }
}