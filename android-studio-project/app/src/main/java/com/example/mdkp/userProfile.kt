package com.example.mdkp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import classes.usersClass
import com.example.mdkp.databinding.ActivityUserProfileBinding

class userProfile : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.getParcelableExtra<usersClass>("user")
        binding = ActivityUserProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val bundle = Bundle()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        reviewsFromUsersFragment().arguments = bundle
        bundle.putString("reviews", data?.name + " " + data?.lastname)
        transaction.replace(R.id.fragmentContainerView,reviewsFromUsersFragment())
        transaction.commit()

        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val fragment = reviewsFromUsersFragment()
                    val args = Bundle()
                    args.putString(reviewsFromUsersFragment.ARG_STRING, data?.name + " " + data?.lastname)
                    fragment.arguments = args

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                R.id.chat -> finish()
                R.id.zaivka -> loadFragment(clientZaivka(), data!!)
                R.id.profile -> loadFragment(profileFragment(), data!!)
                else -> false
            }
            true
        }
    }

    private  fun loadFragment(fragment: Fragment, data: usersClass){
        val bundle = Bundle()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        fragment.arguments = bundle
        bundle.putParcelable("user", data)
        transaction.replace(R.id.fragmentContainerView,fragment)
        transaction.commit()
    }
}