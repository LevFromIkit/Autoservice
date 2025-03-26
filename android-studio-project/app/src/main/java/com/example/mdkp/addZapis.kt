package com.example.mdkp

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import classes.SmenaClass
import classes.adminsClass
import classes.serviceClass
import classes.zaivkaClass
import com.example.mdkp.databinding.FragmentAddZapisBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class addZapis : Fragment() {

    lateinit var binding: FragmentAddZapisBinding
    private lateinit var dataList: ArrayList<String>
    private lateinit var timeList: ArrayList<String>
    private lateinit var timeZaivList: ArrayList<String>
    private lateinit var smenList: ArrayList<SmenaClass>
    private lateinit var zapisList: ArrayList<zaivkaClass>
    private lateinit var serviceList: ArrayList<serviceClass>
    private lateinit var serviceNameList: ArrayList<String>
    private lateinit var zapisAdapter: zapisAdapter
    private lateinit var timeZapisAdapter: timeZapisAdapter
    private val database = FirebaseDatabase.getInstance()
    private val referenceSmen = database.getReference("smens")
    private val referenceService = database.getReference("service")
    private val referenceZapis = database.getReference("zapis")
    private var dateUser: String? = null
    private var timeUser: String? = null
    var serviceId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddZapisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clientId = arguments?.getString("clientId")

        val items = arrayOf("двигатель", "подвеска", "трансмиссия", "плановое ТО", "диагностика", "другое")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinner2.adapter = adapter

        val recyclerView = binding.resView
        val timeRecyclerView = binding.timeRes
        val layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = layoutManager
        val layoutManager2 = GridLayoutManager(requireContext(), 3)
        timeRecyclerView.layoutManager = layoutManager2

        zapisList = ArrayList()
        dataList = ArrayList()
        timeZaivList = ArrayList()
        timeList = ArrayList()
        smenList = ArrayList()
        serviceList = ArrayList()
        serviceNameList = ArrayList()


        referenceZapis.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val smena = snapshot.getValue(zaivkaClass::class.java)

                    dataList.add(smena?.dayAndMonth + "." + smena?.year)
                }
                if (smenList.isEmpty()) {
                    dataList.add("нет доступного времени")
                }
                zapisAdapter.notifyDataSetChanged() // Обновление адаптера
            }

            override fun onCancelled(databaseError: DatabaseError) {
                dataList.add("Error")
                zapisAdapter.notifyDataSetChanged() // Обновление адаптера
            }
        })

        binding.topAppBar.setOnClickListener{
            requireActivity().onBackPressed()
        }

        zapisAdapter = zapisAdapter(dataList)
        recyclerView.adapter = zapisAdapter

        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                dataList.clear()
                smenList.clear()
                serviceList.clear()
                serviceNameList.clear()

                referenceService.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val service = snapshot.getValue(serviceClass::class.java)
                            if (service?.speciliality == items[position]) {
                                serviceList.add(service!!)
                                serviceNameList.add(service?.name!!)
                            }
                        }
                        if (serviceList.isEmpty()) {
                            serviceNameList.add("нету")
                        }

                        val adapter2 = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, serviceNameList)
                        adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                        binding.spinner4.adapter = adapter2
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        serviceNameList.add("Error")
                    }
                })

                referenceSmen.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val smena = snapshot.getValue(SmenaClass::class.java)
                            smenList.add(smena!!)
                            dataList.add(String.format("%02d.%02d.%04d", smena.day, smena.mounth, smena.year))
                        }
                        if (smenList.isEmpty()) {
                            dataList.add("нет доступного времени")
                        }
                        zapisAdapter.notifyDataSetChanged() // Обновление адаптера
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        dataList.add("Error")
                        zapisAdapter.notifyDataSetChanged() // Обновление адаптера
                    }
                })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spinner4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                serviceId = serviceList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                serviceId = null
            }
        }

        timeZapisAdapter = timeZapisAdapter(timeList)
        timeRecyclerView.adapter = timeZapisAdapter

        zapisAdapter.onItemClick = {

            dateUser = it
            binding.textView8.text = "Выбранное время: " + it
            timeList.clear()

            referenceZapis.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val zapis = snapshot.getValue(zaivkaClass::class.java)
                        if (zapis?.dayAndMonth == it) {
                            zapisList.add(zapis!!)
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

            for (time in smenList){
                if (it.split(".")[0] + "." + it.split(".")[1] == String.format("%02d.%02d", time.day, time.mounth)) {
                    val start = time.hourStart!! * 100 + time.minuteStart!!
                    val finish = time.hourEnd!! * 100 + time.minuteEnd!!
                    var hm = start
                    while (hm < finish){
                        val hour = hm / 100
                        val minute = hm % 100
                        timeList.add(String.format("%02d:%02d", hour, minute))
                        if (minute + 15 < 60) {
                            hm += 15
                        } else {
                            hm += 55
                        }
                    }
                    timeList.add(String.format("%02d:%02d", hm / 100, hm % 100))
                }
            }
            for (i in zapisList){
                val index = timeList.indexOf(i.hourAndMinute)
                if (index != -1)
                    timeList.removeAt(index)
            }
            timeZapisAdapter.notifyDataSetChanged()
        }
        timeZapisAdapter.onItemClick = {
            timeUser = it
            binding.textView9.text = "Выбранное время: " + timeUser
            timeList.clear()
            timeZapisAdapter.notifyDataSetChanged()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.example.mdkp.R.id.complete -> {
                    var describe = binding.deskribe.text.toString()
                    if (describe == "")
                        describe = "нет пожеланий"
                    if (timeUser != null && dateUser != null && describe != null){
                        val id = referenceZapis.push().key!!

                        val zapis = zaivkaClass(id, "---", clientId, serviceId, dateUser, dateUser!!.split(".").last(), timeUser, describe, "---", "---", "---")
                        referenceZapis.child(id).setValue(zapis)
                            .addOnSuccessListener{
                                Toast.makeText(requireContext(), getString(com.example.mdkp.R.string.succesful), Toast.LENGTH_SHORT).show()
                                requireActivity().onBackPressed()
                            }.addOnFailureListener {
                                Toast.makeText(requireContext(), getString(com.example.mdkp.R.string.error), Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                else -> false
            }
            true
        }
    }
}