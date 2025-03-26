package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import classes.adminsClass
import classes.usersClass
import com.example.mdkp.databinding.FragmentAdminInfoBinding


class adminInfo : Fragment() {

    lateinit var binding: FragmentAdminInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<adminsClass>("admin")
        if (data != null){

            binding.nameAdmin.text = getString(R.string.name)+ ": " + data?.name
            binding.lastnameAdmin.text = getString(R.string.lastName)+ ": " + data?.lastname
            binding.emailAdmin.text = getString(R.string.email) + ": " + data?.email
            binding.phoneAdmin.text = getString(R.string.phone) + ": +7" + data?.phone
            binding.loginAdmin.text = getString(R.string.login) + ": " + data?.login
        }

        binding.topAppBar.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}