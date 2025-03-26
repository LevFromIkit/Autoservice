package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class timeZapisAdapter(private val timeList: ArrayList<String>): RecyclerView.Adapter<timeZapisAdapter.timeZapisViewHolder>(){

    var onItemClick : ((String) -> Unit)? = null

    class timeZapisViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val time : TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): timeZapisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_time_zapis_adapter, parent, false)
        return timeZapisViewHolder(view)
    }

    override fun getItemCount(): Int {
        return timeList.size
    }

    override fun onBindViewHolder(holder: timeZapisViewHolder, position: Int) {
        val time = timeList[position]
        holder.time.text = time
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(time)
        }
    }
}