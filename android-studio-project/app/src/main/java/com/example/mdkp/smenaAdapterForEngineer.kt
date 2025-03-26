package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.SmenaClass

class smenaAdapterForEngineer(private val smenaList: ArrayList<SmenaClass>): RecyclerView.Adapter<smenaAdapterForEngineer.smenaViewHolder>(){

    var onItemClick : ((SmenaClass) -> Unit)? = null

    class smenaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date : TextView = itemView.findViewById(R.id.date)
        val timeS : TextView = itemView.findViewById(R.id.timeFrom)
        val timeF : TextView = itemView.findViewById(R.id.timeTo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): smenaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_smens_adapter, parent, false)
        return smenaViewHolder(view)
    }

    override fun onBindViewHolder(holder: smenaViewHolder, position: Int) {
        val smena = smenaList[position]

        holder.date.text = String.format("%02d.%02d.%04d", smena.day, smena.mounth, smena.year)
        holder.timeS.text = String.format("%02d:%02d", smena.hourStart, smena.minuteStart)
        holder.timeF.text = String.format("%02d:%02d", smena.hourEnd, smena.minuteEnd)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(smena)
        }
    }

    override fun getItemCount(): Int {
        return smenaList.size
    }

}