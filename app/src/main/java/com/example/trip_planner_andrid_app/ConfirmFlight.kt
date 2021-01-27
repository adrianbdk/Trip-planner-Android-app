package com.example.trip_planner_andrid_app
import SeatIdAdapter
import SeatIdAdapterTwoWay
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trip_planner_andrid_app.flights.data.FlightData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.modal_confirm_seats.*
import java.security.MessageDigest
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ConfirmFlight: AppCompatActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var mRecyclerViewTwoWay: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mAdapterTwoWay: RecyclerView.Adapter<*>? = null
    private var listOfSeatsOneWay: ArrayList<Seat> = ArrayList()
    private var listOfSeatsTwoWay: ArrayList<Seat> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modal_confirm_seats)
        val seatClass = intent.getStringExtra("class")
        val seatValuesOneWay = intent.getSerializableExtra("idsFirstPlane") as? SeatValue

        val seatValuesTwoWay = intent.getSerializableExtra("ids") as? SeatValue
        val inboundDateString = intent.getStringExtra("inboundDateString")

        for(seat in seatValuesOneWay?.value!!){
            val seatInfo = Seat()
            seatInfo.id = seat
            seatInfo.seatClass = seatClass
            listOfSeatsOneWay.add(seatInfo)
        }
        for(seat in listOfSeatsOneWay){
            println(seat.id)
            println(seat.seatClass)
        }

        if(!inboundDateString.equals(null)){
            for(seat in seatValuesTwoWay?.value!!){
                val seatInfo = Seat()
                seatInfo.id = seat
                seatInfo.seatClass = seatClass
                listOfSeatsTwoWay.add(seatInfo)
            }
        }
        mRecyclerView = findViewById(R.id.recyclerview)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView!!.layoutManager = mLayoutManager
        mAdapter = SeatIdAdapter(listOfSeatsOneWay)
        mRecyclerView!!.adapter = mAdapter

        if(!inboundDateString.equals(null)){
            mRecyclerViewTwoWay = findViewById(R.id.recyclerview_twoway)
            val mLayoutManagerTwoWay = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            mRecyclerViewTwoWay!!.layoutManager = mLayoutManagerTwoWay
            mAdapterTwoWay = SeatIdAdapter(listOfSeatsTwoWay)
            mRecyclerViewTwoWay!!.adapter = mAdapterTwoWay
        }

        val flightData = intent.getSerializableExtra("flightData") as FlightData

        var price: Double = flightData.price.toDouble()
        if ((seatClass.toString()) == "Klasa Business") {
            price *= 2.50
        } else if ((seatClass.toString()) == "Klasa Premium") {
            price *= 1.70
        }
        price *= seatValuesOneWay.value.size
        val currency: String
        findViewById<TextView>(R.id.price).text = "${convertPriceToString(price)} PLN"
        if(!inboundDateString.equals(null)){
            currency = convertPriceToString(price / 2)
        } else {
            currency = convertPriceToString(price)
        }

        findViewById<TextView>(R.id.originIata).text = flightData.originIata
        findViewById<TextView>(R.id.destinationIata).text = flightData.destinationIata
        findViewById<TextView>(R.id.originPlace).text = flightData.originPlace
        findViewById<TextView>(R.id.destinationPlace).text = flightData.destinationPlace
        if(!inboundDateString.equals(null)){
            findViewById<LinearLayout>(R.id.twoway_data).visibility = LinearLayout.VISIBLE
            findViewById<TextView>(R.id.originIata_twoway).text = flightData.destinationIata
            findViewById<TextView>(R.id.destinationIata_twoway).text = flightData.originIata
            findViewById<TextView>(R.id.originPlace_twoway).text = flightData.destinationPlace
            findViewById<TextView>(R.id.destinationPlace_twoway).text = flightData.originPlace
        }else{
        findViewById<LinearLayout>(R.id.twoway_data).visibility = LinearLayout.GONE
        }
        println("timeeeee ->>>>> ${flightData.time}")
        if(flightData.time == ""){
            findViewById<TextView>(R.id.timeOutbound).text = "9:50"
        }
        else{
            findViewById<TextView>(R.id.timeOutbound).text = flightData.time
            if(!inboundDateString.equals(null)) {
                findViewById<TextView>(R.id.timeOutbound_twoway).text = flightData.time
            }
        }

        val weekDayFormat = SimpleDateFormat("EEEE")
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var d: Date = sdf.parse(flightData.date.substring(0, 10))
        var dayOfTheWeek: String = weekDayFormat.format(d)
        findViewById<TextView>(R.id.weekDay).text = dayOfTheWeek
        findViewById<TextView>(R.id.date).text = flightData.date.substring(0, 10)

        if(!inboundDateString.equals(null)){
            d = sdf.parse(inboundDateString?.substring(0, 10))
            dayOfTheWeek = weekDayFormat.format(d)
            findViewById<TextView>(R.id.weekDay_twoway).text = dayOfTheWeek
            findViewById<TextView>(R.id.date_twoway).text = inboundDateString?.substring(0, 10)
        }


        payButton.setOnClickListener {
            saveUserFlight(
                flightData.originPlace,
                flightData.destinationPlace,
                flightData.originIata,
                flightData.destinationIata,
                flightData.date.substring(0, 10),
                currency,
                flightData.carrier,
                flightData.time,
                seatClass.toString(),
                seatValuesOneWay.value
            )
            if(!inboundDateString.equals(null)){
                saveUserFlight(
                    flightData.destinationPlace,
                    flightData.originPlace,
                    flightData.destinationIata,
                    flightData.originIata,
                    inboundDateString?.substring(0, 10).toString(),
                    currency,
                    flightData.carrierTwoWay,
                    flightData.time,
                    seatClass.toString(),
                    seatValuesTwoWay?.value!!
                )
            }
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("price", "${convertPriceToString(price)} PLN")
            intent.putExtra("city", flightData.destinationPlace)
            setIntent(intent)
            startActivity(intent)
        }

        val searchButton: Button = findViewById(R.id.searchButton)
        searchButton.visibility = View.GONE
        searchButton.isEnabled = false
    }

    private fun convertPriceToString(price: Double) : String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        var currency: String = format.format(price)
        currency = currency.substring(0, currency.length - 3)
        while (!currency[0].isDigit()) {
            currency = currency.substring(1, currency.length)
        }
        return currency
    }

    private fun saveUserFlight(
        origin: String,
        dest: String,
        originIata: String,
        destIata: String,
        date: String,
        price: String,
        carrier: String,
        hour: String,
        seatClass: String,
        seatArray: ArrayList<String>
    ) {
        val db = Firebase.firestore
        val flight: MutableMap<String, Any> = HashMap()
        flight["origin_place"] = origin
        flight["dest_place"] = dest
        flight["origin_iata"] = originIata
        flight["dest_iata"] = destIata
        flight["date"] = date
        flight["price"] = price
        flight["carrier"] = carrier
        flight["hour"] = hour
        flight["seatClass"] = seatClass
        flight["seatArray"] = seatArray

        val stringToHash = origin + dest + date + price + seatClass + seatArray.toString()
        val flightHash = md5(stringToHash).toHex()
//        val currentUserID = "test123jyim5xqJsrQxB54Xr3w0IRHiA5r2"
        val currentUserID = Firebase.auth.currentUser?.uid.toString()


        db.collection("flights").document(flightHash)
            .set(flight)
            .addOnSuccessListener {
                db.collection("users").document(currentUserID)
                    .update("flights", FieldValue.arrayUnion(flightHash))
                    .addOnSuccessListener {
                        println("OK")
                    }
                    .addOnFailureListener {
                        println("NOT OK")
                    }
            }
            .addOnFailureListener{
                println("NOT OK")
            }
    }

    fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(Charsets.UTF_8))
    fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }
}