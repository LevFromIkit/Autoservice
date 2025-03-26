package com.example.mdkp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import classes.engineersClass
import com.example.mdkp.databinding.ActivityEngineerProfileBinding

class engineerProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEngineerProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = intent.getParcelableExtra<engineersClass>("engineer")
        binding = ActivityEngineerProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val bundle = Bundle()
        bundle.putString("revieww", "${data?.name} ${data?.lastname}")
        reviewsFromUsersFragment().arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, reviewsFromUsersFragment())
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
                R.id.smen -> {
                    val fragment = smenEng()
                    val args = Bundle()
                    args.putString(smenEng.ARG_STRING, data?.id)
                    fragment.arguments = args

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                R.id.chat -> finish()
                R.id.profile -> loadFragment(engineerProfileFragment(), data!!)
                R.id.zaivka -> {
                    loadFragment(zaivkiEng(), data!!)
                }
                else -> false
            }
            true
        }
    }

    private  fun loadFragment(fragment: Fragment, data: engineersClass){
        val bundle = Bundle()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        fragment.arguments = bundle
        bundle.putParcelable("engineer", data)
        transaction.replace(R.id.fragmentContainerView,fragment)
        transaction.commit()
    }
}