package com.example.trip_planner_andrid_app
import SeatIdAdapter
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.payment_success_screen.*
import java.util.*


class ProfileFlightsDetails: AppCompatActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    var listOfSeats: ArrayList<Seat> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modal_confirm_seats)
        val seatClass = intent.getStringExtra("class")
        val departurePlaceIntent = intent.getStringExtra("wylot_z")
        val arrivalPlaceIntent = intent.getStringExtra("przylot_do")
        val priceIntent = intent.getStringExtra("cena")
        val departureDateIntent = intent.getStringExtra("data_wylotu")
        val originIata = intent.getStringExtra("originIata")
        val destinationIata = intent.getStringExtra("destinationIata")
        val seatValues = intent.getSerializableExtra("ids") as? SeatValue

        var originIataTextView: TextView = findViewById(R.id.originIata)
        var destinationIataTextView: TextView = findViewById(R.id.destinationIata)

        originIataTextView.text = originIata
        destinationIataTextView.text = destinationIata

        var payButton: Button = findViewById(R.id.payButton)
//        val payButtonBackground : FrameLayout = findViewById(R.id.payButtonBackground)
        payButton.visibility = View.GONE
        payButton.isEnabled = false
//        payButtonBackground.visibility = View.GONE

        for(seat in seatValues?.value!!){
//            println(seat)
//            println(seatClass)
            val seatInfo = Seat()
            seatInfo.id = seat
            seatInfo.seatClass = seatClass
            listOfSeats.add(seatInfo)
        }
        for(seat in listOfSeats){
            println(seat.id)
            println(seat.seatClass)
        }
        mRecyclerView = findViewById(R.id.recyclerview)
        val departurePlace: TextView = findViewById(R.id.originPlace)
        val arrivalPlace: TextView = findViewById(R.id.destinationPlace)
        val price: TextView = findViewById(R.id.price)
        val departureDate: TextView = findViewById(R.id.date)
        findViewById<TextView>(R.id.weekDay).text = getDayName(departureDateIntent);
        departurePlace.text = departurePlaceIntent
        arrivalPlace.text = arrivalPlaceIntent
        price.text = "$priceIntent PLN"
        departureDate.text = departureDateIntent


        var mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = SeatIdAdapter(listOfSeats)
        mRecyclerView!!.adapter = mAdapter

        val searchButton: Button = findViewById(R.id.searchButton)
        searchButton.visibility = View.VISIBLE
        searchButton.isEnabled = true
        searchButton.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.google.com/maps/search/$arrivalPlaceIntent+hotel/")
            startActivity(openURL)
        }
    }

    private fun getDayName(flightData: String?): String {
        val weekDayFormat = SimpleDateFormat("EEEE")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val d: Date = sdf.parse(flightData?.substring(0, 10))
        return weekDayFormat.format(d);
    }
}