package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import classes.adminsClass
import com.example.mdkp.databinding.FragmentAdminProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class adminProfileFragment : Fragment() {

    lateinit var bindingg: FragmentAdminProfileBinding

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

        val databaseReference = FirebaseDatabase.getInstance().reference

        val data = arguments?.getParcelable<adminsClass>("admin")
        if (data != null) {
            bindingg.nameUser.text = getString(R.string.name) + ": " + data?.name
            bindingg.lastnameUser.text = getString(R.string.lastName) + ": " + data?.lastname
            bindingg.emailUser.text = getString(R.string.email) + ": " + data?.email
            bindingg.phoneUser.text = getString(R.string.phone) + ": +7" + data?.phone
            bindingg.login.text = getString(R.string.login) + ": " + data?.login
        }

        bindingg.changePasBut.setOnClickListener {
            val newPas = bindingg.passwordView.text.toString()
            val reNewPas = bindingg.repeatPasswordView.text.toString()

            if (newPas.isEmpty() or reNewPas.isEmpty()){
                Toast.makeText(requireContext(), R.string.infillNecc, Toast.LENGTH_SHORT).show()
            }else if (newPas != reNewPas){
                Toast.makeText(requireContext(), R.string.reg_repeatPasswordError, Toast.LENGTH_SHORT).show()
            } else {
                val databaseReference = FirebaseDatabase.getInstance().reference
                val userId = data?.id
                if (userId != null) {
                    databaseReference.child("admin").child(userId).child("password")
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