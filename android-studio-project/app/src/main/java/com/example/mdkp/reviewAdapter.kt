package com.example.mdkp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.reviewClass

class reviewAdapter(private val reviewList: ArrayList<reviewClass>): RecyclerView.Adapter<reviewAdapter.reviewViewHolder>() {

    var onItemClick : ((reviewClass) -> Unit)? = null

    class reviewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleText : TextView = itemView.findViewById(R.id.textTitle)
        val textMark : TextView = itemView.findViewById(R.id.textMark)
        val textM = itemView.context.getString(R.string.mark) + " "
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): reviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_adapter, parent, false)
        return reviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: reviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.titleText.text = review.title
        holder.textMark.text = holder.textM + review.mark.toString()

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(review)
        }
    }
}