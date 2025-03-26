package com.example.mdkp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import classes.adminsClass
import classes.engineersClass
import classes.usersClass
import com.example.mdkp.databinding.ActivityAutorizingBinding
import com.google.firebase.database.*


class autorizing : AppCompatActivity() {

    private lateinit var binding: ActivityAutorizingBinding
    private val database = FirebaseDatabase.getInstance()
    private val referenceUser = database.getReference("user")
    private val referenceAdmin = database.getReference("admin")
    private val referenceEngineer = database.getReference("engineer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorizing)

        binding = ActivityAutorizingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.registerButton.setOnClickListener(){
            val intent = Intent(this, registrating::class.java)
            startActivity(intent)
        }

        binding.forgotView.setOnClickListener{
            val intent = Intent(this, forgotPassword::class.java)
            startActivity(intent)
        }

        binding.logInBut.setOnClickListener {
            val login = binding.loginText.text
            if (isGood()){
                if (login!!.matches(("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+").toRegex()) || "+79" in login)
                    findUser()
                else if ("admin" in login)
                    findAdmin()
                else
                    findEngineer()
            }else
                Toast.makeText(this, getString(R.string.reg_logInvalid), Toast.LENGTH_SHORT).show()
            }
        }

    private fun isGood(): Boolean{
        val textViewLogin = binding.loginText
        val textViewPassword = binding.loginPassword

        if (textViewLogin.text.isNullOrBlank()){
            textViewLogin.setError(getString(R.string.reg_logError))
            return false
        }

        if (textViewPassword.text.isNullOrBlank()){
            textViewPassword.setError(getString(R.string.reg_logError))
            return false
        }
        return true
    }

    private fun findUser(){
        val login = binding.loginText.text.toString()
        val password = binding.loginPassword.text.toString()

        referenceUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var findUser = false
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(usersClass::class.java)

                    if (("+7" + user!!.phone == login || user!!.email == login) && user!!.password == password){
                        if (user.faild == false) {
                            val intent = Intent(this@autorizing, userProfile::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            findUser = true
                        }else{
                            findUser = true
                            Toast.makeText(this@autorizing, getString(R.string.vBane), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                if (!findUser)
                    Toast.makeText(this@autorizing, getString(R.string.noFindUser), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@autorizing, getString(R.string.serverError), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun findAdmin(){
        val login = binding.loginText.text.toString()
        val password = binding.loginPassword.text.toString()

        referenceAdmin.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var findAdmin = false
                for (snapshot in dataSnapshot.children) {
                    val admin = snapshot.getValue(adminsClass::class.java)

                    if (admin!!.login == login && admin!!.password == password){
                        val intent = Intent(this@autorizing, adminProfile::class.java)
                        intent.putExtra("admin", admin)
                        startActivity(intent)
                        findAdmin = true
                    }
                }
                if (!findAdmin)
                    Toast.makeText(this@autorizing, getString(R.string.noFindUser), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@autorizing, getString(R.string.serverError), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun findEngineer(){
        val login = binding.loginText.text.toString()
        val password = binding.loginPassword.text.toString()

        referenceEngineer.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var findEngineer = false
                for (snapshot in dataSnapshot.children) {
                    val engineer = snapshot.getValue(engineersClass::class.java)

                    if (engineer!!.login == login && engineer!!.password == password){

                        if (engineer.faild == false) {
                            val intent = Intent(this@autorizing, engineerProfile::class.java)
                            intent.putExtra("engineer", engineer)
                            startActivity(intent)
                            findEngineer = true
                        }else{
                            findEngineer = true
                            Toast.makeText(this@autorizing, getString(R.string.vBane), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                if (!findEngineer)
                    Toast.makeText(this@autorizing, getString(R.string.noFindUser), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@autorizing, getString(R.string.serverError), Toast.LENGTH_SHORT).show()
            }
        })
    }
}