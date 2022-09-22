package com.example.pictionary

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        customizeActionBar()

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.list)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val bundle = intent.extras
        val data: ArrayList<Question>? = (bundle?.getParcelableArrayList<Question>("results"))

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data!!, this)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        displayScore(data)

    }

    private fun displayScore(data: ArrayList<Question>) {
        val demo = data.size
        var nume:Int = 0
        for(q in data){
            if(q.correctAnswer){
                nume++
            }
        }
        score.setText(StringBuilder("Score:"+nume+"/"+demo).toString())
    }

    private fun customizeActionBar() {
        val tv = TextView(applicationContext)
        tv.setText(StringBuilder("Result").toString());
        tv.setGravity(Gravity.CENTER);
        // Create a LayoutParams for TextView
        // Create a LayoutParams for TextView
        val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,  // Width of TextView
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ) // Height of TextView


        // Apply the layout parameters to TextView widget

        // Apply the layout parameters to TextView widget
        tv.layoutParams = lp
        tv.setTextColor(getResources().getColor(R.color.white))
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20F);

        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(tv)
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.black)))
    }
}