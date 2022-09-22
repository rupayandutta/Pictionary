package com.example.pictionary

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.RelativeLayout.LayoutParams
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*


class PlayActivity : AppCompatActivity()  {
   lateinit var questions : Array<Question>
   var round : Int = 0
   var difficulty : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        loadQuestions()
    }

    override fun onResume() {
        super.onResume()
        round = 0
        difficulty = 3
        resetAnswers()
        loadScreen()
    }

    private fun resetAnswers() {
        for(q in questions){
            q.isAsked = false
            q.correctAnswer = false
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun loadScreen() {
      this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)}

        round++;

        name.clearFocus()
        name.getText().clear()
        val currentQuestion : Question?= findNextQuestion(difficulty)
        currentQuestion?.isAsked = true

        name.setHint("Answer")

        customizeActionBar()

        val time = 10+(difficulty-1)*5
        val timer = object : CountDownTimer((time*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(millisUntilFinished >= 10000) {
                    timer.text = StringBuilder("00:" + millisUntilFinished / 1000).toString()
                }else{
                    timer.text = StringBuilder("00:0" + millisUntilFinished / 1000).toString()
                }
            }

            override fun onFinish() {
                difficulty--
                currentQuestion?.correctAnswer = false
                btn_showMessage(2, currentQuestion?.answer!!)
            }
        }
        timer.start()

        currentQuestion?.imageDrawableRes?.let { image.setImageResource(it) }
        name.setOnClickListener {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

       name.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
        })



        submit.setOnClickListener {
            timer.cancel()
            if (name.text.toString().uppercase(Locale.getDefault()) == currentQuestion?.answer) {
                currentQuestion?.correctAnswer = true
                if (difficulty < 5) {
                    difficulty++;
                }
                btn_showMessage(0, "")
            } else {
                currentQuestion?.correctAnswer = false
                difficulty--;
                btn_showMessage(1, currentQuestion?.answer!!)
            }
        }
    }

    // state 0 -> correct, 1-> wrong, 2->timeout
    private fun btn_showMessage(state: Int, correctAnswer : String) {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        val mView: View = layoutInflater.inflate(R.layout.custom_dialog, null)
        val textView: TextView = mView.findViewById<View>(R.id.msg_text) as TextView
        val btn_okay: Button = mView.findViewById<View>(R.id.btn_okay) as Button
        val annimation : LottieAnimationView = mView.findViewById<View>(R.id.animationView) as LottieAnimationView
        alert.setView(mView)
        val alertDialog: AlertDialog = alert.create()
        alertDialog.setCanceledOnTouchOutside(false)

        if(state == 0) {
            annimation.setAnimation(R.raw.tick)
            textView.visibility = View.GONE
        }else if(state == 1){
            annimation.setAnimation(R.raw.cross)
            textView.setText("Wrong !! Correct Answer : "+correctAnswer)
            textView.visibility = View.VISIBLE
        }else{
            annimation.setAnimation(R.raw.timeout)
            textView.setText("Timeout !! Correct Answer : "+correctAnswer)
            textView.visibility = View.VISIBLE
        }

        if(difficulty == 0 || round == 5){
            btn_okay.text = "Game Over, Show Result"
        }else{
            btn_okay.text = "Next Question"
        }

        btn_okay.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
            if(difficulty == 0 || round == 5){
                launchResultScreen()
            }else{
                loadScreen()
            }
        })
        alertDialog.show()
    }

    private fun launchResultScreen() {
        val intent = Intent(this, ResultActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelableArrayList("results", prepareResult())
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun customizeActionBar() {
        val tv = TextView(applicationContext)
        tv.setText(StringBuilder("Round "+round+"/5").toString());
        tv.setGravity(Gravity.CENTER);
        val lp: LayoutParams = RelativeLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,  // Width of TextView
            LayoutParams.WRAP_CONTENT
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

    private fun findNextQuestion(difficulty: Int): Question? {
        val listQuestions : ArrayList<Question> = ArrayList()
        for(q in questions){
            if(q.difficulty == difficulty && q.isAsked == false){
                listQuestions.add(q)
            }
        }
        val size = listQuestions.size
        val random = (0..size-1).random()
        return listQuestions.get(random)
    }

    private fun prepareResult() : ArrayList<Question>{
        val results = ArrayList<Question>()
        for(q in questions){
            if(q.isAsked){
                results.add(q)
            }
        }
        return results;
    }

    private fun loadQuestions() {
        questions = arrayOf<Question>(
            Question(  R.drawable.dog, 1, "DOG"),
            Question(   R.drawable.cat, 1, "CAT"),
            Question(   R.drawable.horse, 1, "HORSE"),
            Question(   R.drawable.hen, 1, "HEN"),
            Question(   R.drawable.fish, 1, "FISH"),
            Question(   R.drawable.bear, 2, "BEAR"),
            Question(   R.drawable.bird, 2, "BIRD"),
            Question(   R.drawable.shark, 2, "SHARK"),
            Question(   R.drawable.snake, 2, "SNAKE"),
            Question(   R.drawable.pig, 2, "PIG"),
            Question(   R.drawable.lion, 3, "LION"),
            Question(   R.drawable.turkey, 3, "TURKEY"),
            Question(   R.drawable.wolf, 3, "WOLF"),
            Question(   R.drawable.spider, 3, "SPIDER"),
            Question(   R.drawable.rabbit, 3, "RABBIT"),
            Question(   R.drawable.duck, 4, "DUCK"),
            Question(   R.drawable.deer, 4, "DEER"),
            Question(   R.drawable.cow, 4, "COW"),
            Question(   R.drawable.monkey, 4, "MONKEY"),
            Question(   R.drawable.lobster, 4, "LOBSTER"),
            Question(   R.drawable.ape, 5, "APE"),
            Question(   R.drawable.pony, 5, "PONY"),
            Question(   R.drawable.eagle, 5, "EAGLE"),
            Question(   R.drawable.dolphin, 5, "DOLPHIN"),
            Question(   R.drawable.bison, 5, "BISON"))
    }


}