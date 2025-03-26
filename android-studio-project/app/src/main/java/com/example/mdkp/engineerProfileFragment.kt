package com.example.mdkp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import classes.adminsClass
import classes.engineersClass
import com.example.mdkp.databinding.FragmentAdminProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class engineerProfileFragment : Fragment(){
    lateinit var bindingg: FragmentAdminProfileBinding
    private val database = FirebaseDatabase.getInstance()
    private val referenceEngineer = database.getReference("engineer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingg = FragmentAdminProfileBinding.inflate(inflater, container, false)
        return bindingg.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var data = arguments?.getParcelable<engineersClass>("engineer")
        if (data != null) {
            bindingg.nameUser.text = getString(R.string.name) + ": " + data?.name
            bindingg.lastnameUser.text = getString(R.string.lastName) + ": " + data?.lastname
            bindingg.emailUser.text = getString(R.string.email) + ": " + data?.email
            bindingg.phoneUser.text = getString(R.string.phone) + ": +7" + data?.phone
            bindingg.login.text = getString(R.string.login) + ": " + data?.login
        }

        bindingg.changePasBut.setOnClickListener {
            var key = "1"
            var findAdmin = false

            referenceEngineer.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val admin = snapshot.getValue(engineersClass::class.java)
                        if (admin!!.login.toString() == data?.login.toString()){
                            key = snapshot.key.toString()
                            data?.password = bindingg.repeatPasswordView.text.toString()
                            findAdmin = true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            if (findAdmin) {
                referenceEngineer.child("engineer").child(key).setValue(data)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Данные пользователя обновлены",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка при обновлении данных пользователя",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
}