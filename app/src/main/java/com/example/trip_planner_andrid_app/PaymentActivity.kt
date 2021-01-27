package com.example.trip_planner_andrid_app

import android.animation.Animator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.payment_screen.*
import kotlinx.android.synthetic.main.payment_screen.congratulation
import kotlinx.android.synthetic.main.payment_success_screen.*

class PaymentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_screen)

        val price = intent.getStringExtra("price")
        var priceTextView: TextView = findViewById(R.id.price)


        priceTextView.text = price

        btn_pay.setOnClickListener {
            payment.playAnimation()

        }

        payment.addAnimatorUpdateListener { valueAnimator ->
            // Set animation progress
            val progress = (valueAnimator.animatedValue as Float * 100).toInt()

            if (progress == 59) {
                setContentView(R.layout.payment_success_screen)
                congratulation.setMinAndMaxProgress(0.0f, 0.5f)

                val city = intent.getStringExtra("city")
                val hotelsDesc: TextView = findViewById(R.id.niceText)
                hotelsDesc.text = "Powinieneś rozejrzeć się za hotelem w mieście $city. Czy chcesz poszukać hoteli teraz?"

                btn_later.setOnClickListener {
                    finishAffinity()
                    intent = Intent(this, SearchForFlightsActivity::class.java)
                    intent.flags = Intent.FLAG_FROM_BACKGROUND
                    startActivity(intent)
                    startActivity(Intent(this, ProfileActivity::class.java))
                }

                btn_search.setOnClickListener {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse("https://www.google.com/maps/search/$city+hotel/")
                    startActivity(openURL)
                }
            }
        }


    }

    override fun onBackPressed() {
        return
    }

}
