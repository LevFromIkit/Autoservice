package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.usersClass
import classes.zaivkaClass
import com.example.mdkp.databinding.FragmentClientZaivkaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class clientZaivka : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var binding: FragmentClientZaivkaBinding
    private lateinit var zapisList: ArrayList<zaivkaClass>
    private lateinit var zapisViewAdapter: zapisViewAdapter
    private val database = FirebaseDatabase.getInstance()
    private val referenceZapis = database.getReference("zapis")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientZaivkaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getParcelable<usersClass>("user")
        zapisList = ArrayList()


        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.resView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        zapisViewAdapter = zapisViewAdapter(zapisList)
        recyclerView.adapter = zapisViewAdapter

        zapisViewAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putParcelable("zapis", it)
            val fragment = zapisView()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val items =
            arrayOf("Мои", "Выполненные")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerVibor.adapter = adapter

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addUser -> {
                    val bundle = Bundle()
                    val fragment = addZapis()
                    bundle.putString("clientId", data?.id)
                    fragment.arguments = bundle
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                else -> false
            }
            true
        }

        binding.spinnerVibor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                zapisList.clear()
                referenceZapis.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val zapis = snapshot.getValue(zaivkaClass::class.java)
                            if ((zapis?.userId == data?.id && position == 0 && zapis?.finish == false) || (zapis?.userId == data?.id && position == 1 && zapis?.finish == true))
                                zapisList.add(zapis!!)
                        }
                        if (zapisList.isEmpty()) {
                            Toast.makeText(requireContext(), "нет записей", Toast.LENGTH_SHORT).show()
                        }
                        zapisViewAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(requireContext(), "нет записей", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}