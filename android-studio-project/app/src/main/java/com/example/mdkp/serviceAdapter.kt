package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.serviceClass
import com.google.firebase.database.FirebaseDatabase


class serviceAdapter(private val serviceList: ArrayList<serviceClass>): RecyclerView.Adapter<serviceAdapter.serviceViewHolder>(){

    var onItemClick : ((serviceClass) -> Unit)? = null

    interface OnItemClickListener {
        fun onItemClick(service: serviceClass)
    }

    class serviceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
        val speciality : TextView = itemView.findViewById(R.id.speciality)
        val duration : TextView = itemView.findViewById(R.id.duration)
        val cost : TextView = itemView.findViewById(R.id.cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): serviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_service_adapter, parent, false)
        return serviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: serviceViewHolder, position: Int) {
        val service = serviceList[position]

        holder.name.text = service.name
        holder.speciality.text = service.speciliality
        holder.duration.text = "Время: " + service.duration.toString()
        holder.cost.text = "Цена:" + service.cost.toString()

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(service)
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}