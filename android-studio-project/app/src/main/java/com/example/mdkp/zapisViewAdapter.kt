package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.serviceClass
import classes.zaivkaClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class zapisViewAdapter(private val zapisList: ArrayList<zaivkaClass>): RecyclerView.Adapter<zapisViewAdapter.zapisViewViewHolder>(){


    private val database = FirebaseDatabase.getInstance()
    private val referenceService = database.getReference("service")

    var onItemClick : ((zaivkaClass) -> Unit)? = null

    class zapisViewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
        val date : TextView = itemView.findViewById(R.id.date)
        val time : TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): zapisViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_zapis_view_adapter, parent, false)
        return zapisViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: zapisViewViewHolder, position: Int) {

        val zapis = zapisList[position]
        holder.name.text = "---"

        referenceService.orderByChild("id").equalTo(zapis.serviceId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val service = snapshot.getValue(serviceClass::class.java)
                    holder.name.text = service!!.name
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        holder.date.text = "Дата: " + zapis.dayAndMonth
        holder.time.text = "Время: " + zapis.hourAndMinute
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(zapis)
        }
    }

    override fun getItemCount(): Int {
        return zapisList.size
    }
}