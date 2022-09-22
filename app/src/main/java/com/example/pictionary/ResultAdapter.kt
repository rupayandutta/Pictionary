package com.example.pictionary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_play.*

class CustomAdapter(private val mList: ArrayList<Question>, private val mContext: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentQuestion = mList[position]

        // sets the image to the imageview from our itemHolder class
        currentQuestion?.imageDrawableRes?.let { holder.imageView.setImageResource(it) }

        // sets the text to the textview from our itemHolder class
        holder.correct_name.text = currentQuestion.answer

        holder.result.text = if(currentQuestion.correctAnswer) ("CORRECT") else ("INCORRECT")
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val correct_name: TextView = itemView.findViewById(R.id.correct_name)
        val result: TextView = itemView.findViewById(R.id.result)
    }
}