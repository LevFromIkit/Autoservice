package com.example.mdkp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.SmenaClass
import classes.engineersClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class smensAdapter(private val smenaList: ArrayList<SmenaClass>): RecyclerView.Adapter<smensAdapter.smenaViewHolder>(){

    var onItemClick : ((SmenaClass) -> Unit)? = null

    class smenaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
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
        val databaseReference = FirebaseDatabase.getInstance().getReference("engineer")
        holder.name.text = "error"

        if (smena.engineerId != null) {
            databaseReference.child(smena.engineerId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val engineer = dataSnapshot.getValue(engineersClass::class.java)
                        if (engineer != null)
                            holder.name.text = engineer.name + " " + engineer.lastname
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }


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