package com.example.trip_planner_andrid_app

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trip_planner_andrid_app.flights.data.ClassSeatList
import com.example.trip_planner_andrid_app.flights.data.FlightData
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottomsheet_fragment.view.*
import kotlinx.android.synthetic.main.plane_modal.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SelectSeatActivityTwoWay : AppCompatActivity() {
    private val seatsChecked = ArrayList<CheckBox>()
    private val listSeatIds = ArrayList<String>()
    private val seatsEconomyArray = ArrayList<CheckBox>()
    private val seatsPremiumArray = ArrayList<CheckBox>()
    private val seatsBusinessArray = ArrayList<CheckBox>()

    private var seatsHashMap: HashMap<CheckBox, String> = HashMap<CheckBox, String>()

    var seatClass: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plane_modal)

        seatClass = intent.getStringExtra("SeatClass")
        val numberOfAdults = intent.getIntExtra("NumberOfAdults", 0)
        val numberOfKids = intent.getIntExtra("NumberOfKids", 0)
        val totalNumber = numberOfAdults + numberOfKids
        val idsFirstPlane = intent.getSerializableExtra("idsFirstPlane")
        val selectedClass: TextView = this.findViewById(R.id.selected_flight_class) as TextView
        val inboundDateString = intent.getStringExtra("inboundDateString")
        selectedClass.text = seatClass

        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_fragment, null)

        bottomSheetDialog.setContentView(view)

        val bottomSheetInfo = view.findViewById(R.id.seat_info_popup) as TextView

        val intentConfirm = Intent(this, ConfirmFlight::class.java)


        view.btn1.setOnClickListener {
            listSeatIds.clear()
            for (seat in seatsChecked) {
                listSeatIds.add(seatsHashMap.getValue(seat))
            }
            val seatValue = SeatValue(listSeatIds)
            intentConfirm.putExtra("ids", seatValue)
            intentConfirm.putExtra("idsFirstPlane", idsFirstPlane)
            intentConfirm.putExtra("class", seatClass)

            val flightData = intent.getSerializableExtra("flightData") as FlightData
            intentConfirm.putExtra("flightData", flightData)
            intentConfirm.putExtra("inboundDateString", inboundDateString)
            setIntent(intentConfirm)
            startActivity(intentConfirm)
        }

        println("numery $numberOfAdults $numberOfKids $totalNumber")
        println("Klasa siedzeń $seatClass")

        var numberOfCheckboxesChecked = 0
        fun seatListener(seat: CheckBox) {
            seat.setOnClickListener { isChecked ->
                if (numberOfCheckboxesChecked >= totalNumber) {
                    if (seat.isChecked) {
                        seatsChecked.add(seat)
                        seatsChecked[0].isChecked = false
                        seatsChecked.removeAt(0)
                    } else {
                        seatsChecked.remove(seat)
                        numberOfCheckboxesChecked--
                    }
                } else {
                    if (seat.isChecked) {
                        numberOfCheckboxesChecked++
                        seatsChecked.add(seat)
                    } else {
                        numberOfCheckboxesChecked--
                        seatsChecked.remove(seat)
                    }
                }
                if (numberOfCheckboxesChecked == totalNumber.toInt()) {
                    bottomSheetDialog.show()
                }
            }
        }

        seatListener(seatEconomy1)
        seatsHashMap.put(seatEconomy1, "1A")
        seatsEconomyArray.add(seatEconomy1)

        seatListener(seatEconomy2)
        seatsHashMap.put(seatEconomy2, "1B")
        seatsEconomyArray.add(seatEconomy2)

        seatListener(seatEconomy3)
        seatsHashMap.put(seatEconomy3, "1C")
        seatsEconomyArray.add(seatEconomy3)

        seatListener(seatEconomy22)
        seatsHashMap.put(seatEconomy22, "1D")
        seatsEconomyArray.add(seatEconomy22)

        seatListener(seatEconomy23)
        seatsHashMap.put(seatEconomy23, "1E")
        seatsEconomyArray.add(seatEconomy23)

        seatListener(seatEconomy24)
        seatsHashMap.put(seatEconomy24, "1F")
        seatsEconomyArray.add(seatEconomy24)


        seatListener(seatEconomy4)
        seatsHashMap.put(seatEconomy4, "2A")
        seatsEconomyArray.add(seatEconomy4)

        seatListener(seatEconomy5)
        seatsHashMap.put(seatEconomy5, "2B")
        seatsEconomyArray.add(seatEconomy5)

        seatListener(seatEconomy6)
        seatsHashMap.put(seatEconomy6, "2C")
        seatsEconomyArray.add(seatEconomy6)

        seatListener(seatEconomy25)
        seatsHashMap.put(seatEconomy25, "2D")
        seatsEconomyArray.add(seatEconomy25)

        seatListener(seatEconomy26)
        seatsHashMap.put(seatEconomy26, "2E")
        seatsEconomyArray.add(seatEconomy26)

        seatListener(seatEconomy27)
        seatsHashMap.put(seatEconomy27, "2F")
        seatsEconomyArray.add(seatEconomy27)


        seatListener(seatEconomy7)
        seatsHashMap.put(seatEconomy7, "3A")
        seatsEconomyArray.add(seatEconomy7)

        seatListener(seatEconomy8)
        seatsHashMap.put(seatEconomy8, "3B")
        seatsEconomyArray.add(seatEconomy8)

        seatListener(seatEconomy9)
        seatsHashMap.put(seatEconomy9, "3C")
        seatsEconomyArray.add(seatEconomy9)

        seatListener(seatEconomy28)
        seatsHashMap.put(seatEconomy28, "3D")
        seatsEconomyArray.add(seatEconomy28)

        seatListener(seatEconomy29)
        seatsHashMap.put(seatEconomy29, "3E")
        seatsEconomyArray.add(seatEconomy29)

        seatListener(seatEconomy30)
        seatsHashMap.put(seatEconomy30, "3F")
        seatsEconomyArray.add(seatEconomy30)


        seatListener(seatEconomy10)
        seatsHashMap.put(seatEconomy10, "4A")
        seatsEconomyArray.add(seatEconomy10)

        seatListener(seatEconomy11)
        seatsHashMap.put(seatEconomy11, "4B")
        seatsEconomyArray.add(seatEconomy11)

        seatListener(seatEconomy12)
        seatsHashMap.put(seatEconomy12, "4C")
        seatsEconomyArray.add(seatEconomy12)

        seatListener(seatEconomy31)
        seatsHashMap.put(seatEconomy31, "4D")
        seatsEconomyArray.add(seatEconomy31)

        seatListener(seatEconomy32)
        seatsHashMap.put(seatEconomy32, "4E")
        seatsEconomyArray.add(seatEconomy32)

        seatListener(seatEconomy33)
        seatsHashMap.put(seatEconomy33, "4F")
        seatsEconomyArray.add(seatEconomy33)


        seatListener(seatEconomy13)
        seatsHashMap.put(seatEconomy13, "5A")
        seatsEconomyArray.add(seatEconomy13)

        seatListener(seatEconomy14)
        seatsHashMap.put(seatEconomy14, "5B")
        seatsEconomyArray.add(seatEconomy14)

        seatListener(seatEconomy15)
        seatsHashMap.put(seatEconomy15, "5C")
        seatsEconomyArray.add(seatEconomy15)

        seatListener(seatEconomy34)
        seatsHashMap.put(seatEconomy34, "5D")
        seatsEconomyArray.add(seatEconomy34)

        seatListener(seatEconomy35)
        seatsHashMap.put(seatEconomy35, "5E")
        seatsEconomyArray.add(seatEconomy35)

        seatListener(seatEconomy36)
        seatsHashMap.put(seatEconomy36, "5F")
        seatsEconomyArray.add(seatEconomy36)


        seatListener(seatEconomy16)
        seatsHashMap.put(seatEconomy16, "6A")
        seatsEconomyArray.add(seatEconomy16)

        seatListener(seatEconomy17)
        seatsHashMap.put(seatEconomy17, "6B")
        seatsEconomyArray.add(seatEconomy17)

        seatListener(seatEconomy18)
        seatsHashMap.put(seatEconomy18, "6C")
        seatsEconomyArray.add(seatEconomy18)

        seatListener(seatEconomy37)
        seatsHashMap.put(seatEconomy37, "6D")
        seatsEconomyArray.add(seatEconomy37)

        seatListener(seatEconomy38)
        seatsHashMap.put(seatEconomy38, "6E")
        seatsEconomyArray.add(seatEconomy38)

        seatListener(seatEconomy39)
        seatsHashMap.put(seatEconomy39, "6F")
        seatsEconomyArray.add(seatEconomy39)


        seatListener(seatEconomy19)
        seatsHashMap.put(seatEconomy19, "7A")
        seatsEconomyArray.add(seatEconomy19)

        seatListener(seatEconomy20)
        seatsHashMap.put(seatEconomy20, "7B")
        seatsEconomyArray.add(seatEconomy20)

        seatListener(seatEconomy21)
        seatsHashMap.put(seatEconomy21, "7C")
        seatsEconomyArray.add(seatEconomy21)

        seatListener(seatEconomy40)
        seatsHashMap.put(seatEconomy40, "7D")
        seatsEconomyArray.add(seatEconomy40)

        seatListener(seatEconomy41)
        seatsHashMap.put(seatEconomy41, "7E")
        seatsEconomyArray.add(seatEconomy41)

        seatListener(seatEconomy42)
        seatsHashMap.put(seatEconomy42, "7F")
        seatsEconomyArray.add(seatEconomy42)



        seatListener(seatBusiness1)
        seatsHashMap.put(seatBusiness1, "8A")
        seatsBusinessArray.add(seatBusiness1)

        seatListener(seatBusiness2)
        seatsHashMap.put(seatBusiness2, "8B")
        seatsBusinessArray.add(seatBusiness2)

        seatListener(seatBusiness9)
        seatsHashMap.put(seatBusiness9, "8C")
        seatsBusinessArray.add(seatBusiness9)

        seatListener(seatBusiness10)
        seatsHashMap.put(seatBusiness10, "8D")
        seatsBusinessArray.add(seatBusiness10)


        seatListener(seatBusiness3)
        seatsHashMap.put(seatBusiness3, "9A")
        seatsBusinessArray.add(seatBusiness3)

        seatListener(seatBusiness4)
        seatsHashMap.put(seatBusiness4, "9B")
        seatsBusinessArray.add(seatBusiness4)

        seatListener(seatBusiness11)
        seatsHashMap.put(seatBusiness11, "9C")
        seatsBusinessArray.add(seatBusiness11)

        seatListener(seatBusiness12)
        seatsHashMap.put(seatBusiness12, "9D")
        seatsBusinessArray.add(seatBusiness12)


        seatListener(seatBusiness5)
        seatsHashMap.put(seatBusiness5, "10A")
        seatsBusinessArray.add(seatBusiness5)

        seatListener(seatBusiness6)
        seatsHashMap.put(seatBusiness6, "10B")
        seatsBusinessArray.add(seatBusiness6)

        seatListener(seatBusiness13)
        seatsHashMap.put(seatBusiness13, "10C")
        seatsBusinessArray.add(seatBusiness13)

        seatListener(seatBusiness14)
        seatsHashMap.put(seatBusiness14, "10D")
        seatsBusinessArray.add(seatBusiness14)


        seatListener(seatBusiness7)
        seatsHashMap.put(seatBusiness7, "11A")
        seatsBusinessArray.add(seatBusiness7)

        seatListener(seatBusiness8)
        seatsHashMap.put(seatBusiness8, "11B")
        seatsBusinessArray.add(seatBusiness8)

        seatListener(seatBusiness15)
        seatsHashMap.put(seatBusiness15, "11C")
        seatsBusinessArray.add(seatBusiness15)

        seatListener(seatBusiness16)
        seatsHashMap.put(seatBusiness16, "11D")
        seatsBusinessArray.add(seatBusiness16)


        seatListener(seatPremium1)
        seatsHashMap.put(seatPremium1, "12A")
        seatsPremiumArray.add(seatPremium1)

        seatListener(seatPremium2)
        seatsHashMap.put(seatPremium2, "12B")
        seatsPremiumArray.add(seatPremium2)

        seatListener(seatPremium3)
        seatsHashMap.put(seatPremium3, "12C")
        seatsPremiumArray.add(seatPremium3)

        seatListener(seatPremium13)
        seatsHashMap.put(seatPremium13, "12D")
        seatsPremiumArray.add(seatPremium13)

        seatListener(seatPremium14)
        seatsHashMap.put(seatPremium14, "12E")
        seatsPremiumArray.add(seatPremium14)

        seatListener(seatPremium15)
        seatsHashMap.put(seatPremium15, "12F")
        seatsPremiumArray.add(seatPremium15)


        seatListener(seatPremium4)
        seatsHashMap.put(seatPremium4, "13A")
        seatsPremiumArray.add(seatPremium4)

        seatListener(seatPremium5)
        seatsHashMap.put(seatPremium5, "13B")
        seatsPremiumArray.add(seatPremium5)

        seatListener(seatPremium6)
        seatsHashMap.put(seatPremium6, "13C")
        seatsPremiumArray.add(seatPremium6)

        seatListener(seatPremium16)
        seatsHashMap.put(seatPremium16, "13D")
        seatsPremiumArray.add(seatPremium16)

        seatListener(seatPremium17)
        seatsHashMap.put(seatPremium17, "13E")
        seatsPremiumArray.add(seatPremium17)

        seatListener(seatPremium18)
        seatsHashMap.put(seatPremium18, "13F")
        seatsPremiumArray.add(seatPremium18)


        seatListener(seatPremium7)
        seatsHashMap.put(seatPremium7, "14A")
        seatsPremiumArray.add(seatPremium7)

        seatListener(seatPremium8)
        seatsHashMap.put(seatPremium8, "14B")
        seatsPremiumArray.add(seatPremium8)

        seatListener(seatPremium9)
        seatsHashMap.put(seatPremium9, "14C")
        seatsPremiumArray.add(seatPremium9)

        seatListener(seatPremium19)
        seatsHashMap.put(seatPremium19, "14D")
        seatsPremiumArray.add(seatPremium19)

        seatListener(seatPremium20)
        seatsHashMap.put(seatPremium20, "14E")
        seatsPremiumArray.add(seatPremium20)

        seatListener(seatPremium21)
        seatsHashMap.put(seatPremium21, "14F")
        seatsPremiumArray.add(seatPremium21)


        seatListener(seatPremium10)
        seatsHashMap.put(seatPremium10, "15A")
        seatsPremiumArray.add(seatPremium10)

        seatListener(seatPremium11)
        seatsHashMap.put(seatPremium11, "15B")
        seatsPremiumArray.add(seatPremium11)

        seatListener(seatPremium12)
        seatsHashMap.put(seatPremium12, "15C")
        seatsPremiumArray.add(seatPremium12)

        seatListener(seatPremium22)
        seatsHashMap.put(seatPremium22, "15D")
        seatsPremiumArray.add(seatPremium22)

        seatListener(seatPremium23)
        seatsHashMap.put(seatPremium23, "15E")
        seatsPremiumArray.add(seatPremium23)

        seatListener(seatPremium24)
        seatsHashMap.put(seatPremium24, "15F")
        seatsPremiumArray.add(seatPremium24)


        seatListener(seatEconomy43)
        seatsHashMap.put(seatEconomy43, "16A")
        seatsEconomyArray.add(seatEconomy43)

        seatListener(seatEconomy44)
        seatsHashMap.put(seatEconomy44, "16B")
        seatsEconomyArray.add(seatEconomy44)

        seatListener(seatEconomy51)
        seatsHashMap.put(seatEconomy51, "16C")
        seatsEconomyArray.add(seatEconomy51)

        seatListener(seatEconomy52)
        seatsHashMap.put(seatEconomy52, "16D")
        seatsEconomyArray.add(seatEconomy52)


        seatListener(seatEconomy45)
        seatsHashMap.put(seatEconomy45, "17A")
        seatsEconomyArray.add(seatEconomy45)

        seatListener(seatEconomy46)
        seatsHashMap.put(seatEconomy46, "17B")
        seatsEconomyArray.add(seatEconomy46)

        seatListener(seatEconomy53)
        seatsHashMap.put(seatEconomy53, "17C")
        seatsEconomyArray.add(seatEconomy53)

        seatListener(seatEconomy54)
        seatsHashMap.put(seatEconomy54, "17D")
        seatsEconomyArray.add(seatEconomy54)


        seatListener(seatEconomy47)
        seatsHashMap.put(seatEconomy47, "18A")
        seatsEconomyArray.add(seatEconomy47)

        seatListener(seatEconomy48)
        seatsHashMap.put(seatEconomy48, "18B")
        seatsEconomyArray.add(seatEconomy48)

        seatListener(seatEconomy55)
        seatsHashMap.put(seatEconomy55, "18C")
        seatsEconomyArray.add(seatEconomy55)

        seatListener(seatEconomy56)
        seatsHashMap.put(seatEconomy56, "18D")
        seatsEconomyArray.add(seatEconomy56)


        seatListener(seatEconomy49)
        seatsHashMap.put(seatEconomy49, "19A")
        seatsEconomyArray.add(seatEconomy49)

        seatListener(seatEconomy50)
        seatsHashMap.put(seatEconomy50, "19B")
        seatsEconomyArray.add(seatEconomy50)

        seatListener(seatEconomy57)
        seatsHashMap.put(seatEconomy57, "19C")
        seatsEconomyArray.add(seatEconomy57)

        seatListener(seatEconomy58)
        seatsHashMap.put(seatEconomy58, "19D")
        seatsEconomyArray.add(seatEconomy58)

        val reservedEconomySeats = intent.getSerializableExtra("reservedEconomySeatsTwoWays") as ClassSeatList
        val reservedPremiumSeats = intent.getSerializableExtra("reservedPremiumSeatsTwoWays") as ClassSeatList
        val reservedBusinessSeats = intent.getSerializableExtra("reservedBusinessSeatsTwoWays") as ClassSeatList

        prepareRandomSeats(reservedEconomySeats.seats, reservedPremiumSeats.seats, reservedBusinessSeats.seats)
        disableSeats()
    }


    fun prepareRandomSeats(
        reservedEconomySeats: LinkedHashSet<Int>,
        reservedPremiumSeats: LinkedHashSet<Int>,
        reservedBusinessSeats: LinkedHashSet<Int>
    ) {
        for (num in reservedEconomySeats) {
            val reservedSeat: CheckBox = seatsEconomyArray.get(num)
            reservedSeat.setBackgroundColor(R.drawable.bg_seat_plane_reserved)
            reservedSeat.isEnabled = false
            reservedSeat.isClickable = false
        }

        for (num in reservedBusinessSeats) {
            val reservedSeat: CheckBox = seatsBusinessArray.get(num)
            reservedSeat.setBackgroundColor(R.drawable.bg_seat_plane_reserved)
            reservedSeat.isEnabled = false
            reservedSeat.isClickable = false
        }

        for (num in reservedPremiumSeats) {
            val reservedSeat: CheckBox = seatsPremiumArray.get(num)
            reservedSeat.setBackgroundColor(R.drawable.bg_seat_plane_reserved)
            reservedSeat.isEnabled = false
            reservedSeat.isClickable = false
        }
    }

    private fun disableSeats() {
        if (seatClass.equals("Klasa Ekonomiczna")) {
            for (seat in seatsPremiumArray) {
                seat.isEnabled = false
                seat.isClickable = false
                seat.setBackgroundResource(R.drawable.bg_seat_plane_disabled)
            }
            for (seat in seatsBusinessArray) {
                seat.isEnabled = false
                seat.isClickable = false
                seat.setBackgroundResource(R.drawable.bg_seat_plane_disabled)
            }

        } else if (seatClass.equals("Klasa Business")) {
            for (seat in seatsEconomyArray) {
                seat.isEnabled = false
                seat.isClickable = false
                seat.setBackgroundResource(R.drawable.bg_seat_plane_disabled)
            }
            for (seat in seatsPremiumArray) {
                seat.isEnabled = false
                seat.isClickable = false
                seat.setBackgroundResource(R.drawable.bg_seat_plane_disabled)
            }

        } else if (seatClass.equals("Klasa Premium")) {
            for (seat in seatsBusinessArray) {
                seat.isEnabled = false
                seat.isClickable = false
                seat.setBackgroundResource(R.drawable.bg_seat_plane_disabled)
            }
            for (seat in seatsEconomyArray) {
                seat.isEnabled = false
                seat.isClickable = false
                seat.setBackgroundResource(R.drawable.bg_seat_plane_disabled)
            }
        }
    }
}