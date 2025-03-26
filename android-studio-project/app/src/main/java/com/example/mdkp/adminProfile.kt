package com.example.mdkp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import classes.adminsClass
import com.example.mdkp.databinding.ActivityAdminProfileBinding


class adminProfile : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.getParcelableExtra<adminsClass>("admin")
        binding = ActivityAdminProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val bundle = Bundle()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        adminProfileFragment().arguments = bundle
        transaction.replace(R.id.fragmentContainerView,reviewsFromUsersFragment())
        transaction.commit()

        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> loadFragment(adminProfileFragment(), data!!)
                R.id.review -> {
                    val fragment = reviewsFromUsersFragment()
                    val args = Bundle()
                    args.putString(reviewsFromUsersFragment.ARG_STRING, data?.name + " " + data?.lastname)
                    fragment.arguments = args

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                R.id.timetable -> loadFragment(workWithZapis(), data!!)
                R.id.exit -> finish()
                R.id.administrate -> loadFragment(administrate(), data!!)
                else -> loadFragment(profileFragment(), data!!)
            }
            true
        }
    }

    private  fun loadFragment(fragment: Fragment, data: adminsClass){
        val bundle = Bundle()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        fragment.arguments = bundle
        bundle.putParcelable("admin", data)
        transaction.replace(R.id.fragmentContainerView,fragment)
        transaction.commit()
    }
}