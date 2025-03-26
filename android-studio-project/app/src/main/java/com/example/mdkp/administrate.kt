package com.example.mdkp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.adminsClass
import classes.engineersClass
import classes.usersClass
import com.example.mdkp.databinding.FragmentAdministrateBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class administrate : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<usersClass>
    private lateinit var adminList: ArrayList<adminsClass>
    private lateinit var engineerList: ArrayList<engineersClass>
    private lateinit var usersAdapter: userAdapter
    private lateinit var adminsAdapter: adminAdapter
    private lateinit var engineersAdapter: engineerAdapter
    lateinit var binding: FragmentAdministrateBinding
    private val database = FirebaseDatabase.getInstance()
    private val referenceUser = database.getReference("user")
    private val referenceAdmin = database.getReference("admin")
    private val referenceEngineer = database.getReference("engineer")

    private var viewSet: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.administrate_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdministrateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.updateUser -> updateView()
                R.id.addUser -> addUser()
                R.id.adminMenu -> printAdmins()
                R.id.engineerMenu-> printEngineer()
                R.id.clientMenu -> printUsers()
                else -> false
            }
            true
        }
    }

    private fun updateView(){
        when (viewSet) {
            "user" -> printUsers()
            "admin" -> printAdmins()
            "engineer" -> printEngineer()
            else -> Toast.makeText(requireContext(), getString(R.string.userGroup), Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUser(){
        when (viewSet) {
            "user" -> loadFragment(addClient())
            "admin" -> loadFragment(addAdmin())
            "engineer" -> loadFragment(addEngineer())
            else -> Toast.makeText(requireContext(), getString(R.string.userGroup), Toast.LENGTH_SHORT).show()
        }
    }

    private fun printUsers(){

        binding.topAppBar.title = "Клиенты"
        viewSet = "user"

        userList = ArrayList()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.recycler
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        usersAdapter = userAdapter(userList)
        recyclerView.adapter = usersAdapter

        usersAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putParcelable("user", it)
            val fragment = userInfo()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        dateInitializeUser()
    }

    private fun printAdmins() {

        binding.topAppBar.title = "Администраторы"
        viewSet = "admin"

        adminList = ArrayList()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.recycler
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        adminsAdapter = adminAdapter(adminList)
        recyclerView.adapter = adminsAdapter

        adminsAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putParcelable("admin", it)
            val fragment = adminInfo()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        dateInitializeAdmin() // Переместили вызов метода после инициализации адаптера
    }

    private fun dateInitializeAdmin() {

        referenceAdmin.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val admin = snapshot.getValue(adminsClass::class.java)
                    adminList.add(admin!!)
                }
                adminsAdapter.notifyDataSetChanged() // Обновление адаптера
                if (adminList.isEmpty()) {
                    val admin = adminsClass(null, "не", "найдено")
                    adminList.add(admin)
                    adminsAdapter.notifyDataSetChanged() // Обновление адаптера
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val admin = adminsClass(null, "ошибка", "сервера")
                adminList.add(admin)
                adminsAdapter.notifyDataSetChanged() // Обновление адаптера
            }
        })
    }

    private fun printEngineer() {

        binding.topAppBar.title = "Инженеры"
        viewSet = "engineer"

        engineerList = ArrayList()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.recycler
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        engineersAdapter = engineerAdapter(engineerList)
        recyclerView.adapter = engineersAdapter

        engineersAdapter.onItemClick = {
            val bundle = Bundle()
            bundle.putParcelable("engineer", it)
            val fragment = engineerInfo()
            fragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        dateInitializeEngineer()
    }

    private fun dateInitializeEngineer() {

        referenceEngineer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val engineer = snapshot.getValue(engineersClass::class.java)
                    engineerList.add(engineer!!)
                }
                engineersAdapter.notifyDataSetChanged() // Обновление адаптера
                if (engineerList.isEmpty()) {
                    val engineer = engineersClass(null, "не", "найдено")
                    engineerList.add(engineer)
                    engineersAdapter.notifyDataSetChanged() // Обновление адаптера
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val engineer = engineersClass(null, "ошибка", "сервера")
                engineerList.add(engineer)
                engineersAdapter.notifyDataSetChanged() // Обновление адаптера
            }
        })
    }

    private fun dateInitializeUser() {

        referenceUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(usersClass::class.java)
                    userList.add(user!!)
                }
                if (userList.isEmpty()) {
                    val user = usersClass("не", "найдено")
                    userList.add(user)
                }
                usersAdapter.notifyDataSetChanged() // Обновление адаптера
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val user = usersClass("ошибка", "сервера")
                userList.add(user)
                usersAdapter.notifyDataSetChanged() // Обновление адаптера
            }
        })
    }

    private  fun loadFragment(fragment: Fragment){
        val fragment = fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}