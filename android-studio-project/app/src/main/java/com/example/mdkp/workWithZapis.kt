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
import classes.SmenaClass
import classes.engineersClass
import classes.serviceClass
import classes.zaivkaClass
import com.example.mdkp.databinding.FragmentWorkWithZapisBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class workWithZapis : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var binding: FragmentWorkWithZapisBinding
    private lateinit var smenList: ArrayList<SmenaClass>
    private lateinit var smenRecycleList: ArrayList<SmenaClass>
    private lateinit var serviceList: ArrayList<serviceClass>
    private lateinit var engineerList: ArrayList<String>
    private lateinit var engineerNameList: ArrayList<String>
    private lateinit var dayList: ArrayList<String>
    private lateinit var mounthList: ArrayList<String>
    private lateinit var yearList: ArrayList<String>
    private lateinit var smensAdapter: smensAdapter
    private lateinit var serviceAdapter: serviceAdapter
    private var pickName: String = "All"
    private var pickDay: String = "All"
    private var pickMounth: String = "All"
    private var pickYear: String = "All"

    private lateinit var zapisList: ArrayList<zaivkaClass>
    private lateinit var zapisViewAdapter: zapisViewAdapter

    private val database = FirebaseDatabase.getInstance()
    private val referenceSmen = database.getReference("smens")
    private val referenceService = database.getReference("service")
    private val referenceEngineer = database.getReference("engineer")
    private val referenceZapis = database.getReference("zapis")

    private var vibor: String? = "work"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkWithZapisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addUser -> addPlus()
                R.id.work -> printService()
                R.id.history -> printHistory()
                R.id.timetable -> printZapis()
                R.id.addZapis-> loadFragment(addZapis())
                R.id.smens-> printSmenView()
                else -> false
            }
            true
        }
    }

    private fun addPlus(){
        when (vibor) {
            "work" -> loadFragment(serviceCreate())
            else -> false
        }
    }

    private fun loadFragment(fragment: Fragment){

        vibor = "false"

        val fragment = fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun printSmenView(){

        smenList = ArrayList()
        smenRecycleList = ArrayList()
        engineerList = ArrayList()
        engineerList.add("All")
        engineerNameList = ArrayList()
        engineerNameList.add("All")

        dayList = ArrayList()
        mounthList = ArrayList()
        yearList = ArrayList()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, engineerNameList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerVibor.adapter = adapter

        val adapterDay = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dayList)
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDay.adapter = adapterDay

        val adapterMounth = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mounthList)
        adapterMounth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMounth.adapter = adapterMounth

        val adapterYear = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, yearList)
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerYear.adapter = adapterYear

        referenceEngineer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val engineer = snapshot.getValue(engineersClass::class.java)
                    engineerNameList.add(engineer?.name!! + " "+ engineer?.lastname!!)
                    engineerList.add(engineer?.id!!)
                }
                if (engineerList.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.empty), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), getString(R.string.empty), Toast.LENGTH_SHORT).show()
            }
        })

        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.resView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        smensAdapter = smensAdapter(smenRecycleList)
        recyclerView.adapter = smensAdapter

        smensAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putParcelable("smena", it)
            val fragment = smenaInfo()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        referenceSmen.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    val smena = snapshot.getValue(SmenaClass::class.java)
                    smenList.add(smena!!)
                }
                pickName = "All"
                dayList.clear()
                mounthList.clear()
                yearList.clear()

                dayList.add("All")
                mounthList.add("All")
                yearList.add("All")

                for (smena in smenList) {
                    if (!(smena.day.toString() in dayList)) {
                        dayList.add(smena.day.toString())
                    }
                    if (!(smena.mounth.toString() in mounthList)) {
                        mounthList.add(smena.mounth.toString())
                    }
                    if (!(smena.year.toString() in yearList)) {
                        yearList.add(smena.year.toString())
                    }
                }

                adapterDay.notifyDataSetChanged()
                adapterMounth.notifyDataSetChanged()
                adapterYear.notifyDataSetChanged()
                vivodSmen()
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        binding.spinnerVibor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pickName = engineerList[position]
                dayList.clear()
                mounthList.clear()
                yearList.clear()

                dayList.add("All")
                mounthList.add("All")
                yearList.add("All")

                for (smena in smenList) {
                    if (engineerList[position] == "All" || smena.engineerId == engineerList[position]) {
                        if (!(smena.day.toString() in dayList)) {
                            dayList.add(smena.day.toString())
                        }
                        if (!(smena.mounth.toString() in mounthList)) {
                            mounthList.add(smena.mounth.toString())
                        }
                        if (!(smena.year.toString() in yearList)) {
                            yearList.add(smena.year.toString())
                        }
                    }
                }

                adapterDay.notifyDataSetChanged()
                adapterMounth.notifyDataSetChanged()
                adapterYear.notifyDataSetChanged()
                vivodSmen()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Выберите инженера", Toast.LENGTH_SHORT)
            }
        }

        binding.spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pickDay = dayList[position]
                vivodSmen()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Выберите день", Toast.LENGTH_SHORT)
            }
        }

        binding.spinnerMounth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pickMounth = mounthList[position]
                vivodSmen()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Выберите месяц", Toast.LENGTH_SHORT)
            }
        }

        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                pickYear = yearList[position]
                vivodSmen()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Выберите год", Toast.LENGTH_SHORT)
            }
        }
    }

    private fun vivodSmen(){
        smenRecycleList.clear()
        for (smena in smenList) {
            if ((pickName == smena.engineerId || pickName == "All") && (pickDay == smena.day.toString() || pickDay == "All") && (pickMounth == smena.mounth.toString() || pickMounth == "All")
                && (pickYear == smena.year.toString() || pickYear == "All"))
                smenRecycleList.add(smena)
        }
        smensAdapter.notifyDataSetChanged()
    }

    private fun printService() {

        vibor = "work"

        val items =
            arrayOf("All", "двигатель", "подвеска", "трансмиссия", "плановоеТО", "диагностика", "другое")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerVibor.adapter = adapter
        serviceList = ArrayList()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.resView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        serviceAdapter = serviceAdapter(serviceList)
        serviceAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putParcelable("service", it)
            val fragment = serviceInfo()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.spinnerVibor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getSer(items[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                getSer("All")
            }
        }
    }
    private fun getSer(vid: String){
        serviceList = ArrayList()
        serviceAdapter = serviceAdapter(serviceList)
        recyclerView.adapter = serviceAdapter

        referenceService.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val service = snapshot.getValue(serviceClass::class.java)
                    if (service!!.speciliality == vid || vid == "All")
                        serviceList.add(service!!)
                }
                if (serviceList.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.empty), Toast.LENGTH_SHORT).show()
                }
                serviceAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), getString(R.string.empty), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getZap(history: Boolean){
        zapisList = ArrayList()
        smenRecycleList = ArrayList()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.resView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        zapisViewAdapter = zapisViewAdapter(zapisList)
        recyclerView.adapter = zapisViewAdapter

        referenceZapis.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val zapis = snapshot.getValue(zaivkaClass::class.java)
                    if (zapis!!.finish == history)
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

    private fun printZapis(){

        getZap(false)
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
    }

    private fun printHistory(){

        getZap(true)
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
    }
}
