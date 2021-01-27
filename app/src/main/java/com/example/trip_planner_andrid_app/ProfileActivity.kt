package com.example.trip_planner_andrid_app

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.trip_planner_andrid_app.ProfileFlightsDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.profile_activity.*
import java.util.*


class ProfileActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth

    val userCurrentFlights = ArrayList<ArrayList<String>>()
    val userHistoryFlights = ArrayList<ArrayList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        setupNavBar()
        getUserName()

        findBtn.setOnClickListener() {
            startActivity(Intent(this, SearchForFlightsActivity::class.java))
        }

        logout.setOnClickListener() {
            auth.signOut()
            finish()
            startActivity(Intent(this, LoginActivity()::class.java))
        }

        buttonMoreCurrent.setOnClickListener { showAlertDialogFlights(0) }
        buttonMoreHistory.setOnClickListener { showAlertDialogFlights(1) }

        getUserFlights()
    }

    private fun getUserName() {
        val auth: FirebaseAuth = Firebase.auth
        val user = auth.currentUser
        val uid = user?.uid.toString()
        val db = Firebase.firestore

        val docRef = db.collection("users").document(uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document["name"] as String?
                    val userLastname = document["lastname"] as String?
                    findViewById<TextView>(R.id.navFullName).text = "$userName $userLastname"
                    findViewById<TextView>(R.id.navMail).text = user?.email
                    findViewById<TextView>(R.id.profileName).text = "$userName $userLastname"
                } else {
                    println("brak dokumentu obecnego usera")
                }
            }
    }

    private fun setupNavBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navigation_view.setCheckedItem(R.id.ic_profile);
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                    startActivity(Intent(this, SearchForFlightsActivity()::class.java))
                }
                R.id.ic_profile -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.nav_map -> {
                    drawer.closeDrawer(GravityCompat.START)
                    finish()
                    startActivity(Intent(this, MapActivity()::class.java))
                }
                R.id.nav_send -> {
                    drawer.closeDrawer(GravityCompat.START)
                    auth.signOut()
                    finish()
                    startActivity(Intent(this, LoginActivity()::class.java))
                }
            }
            true
        }


        val drawerToggle = ActionBarDrawerToggle(this, findViewById(R.id.drawer), R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showAlertDialogFlights(x: Int) {

        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomDialog)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_flights_list, null)

        println("history of flights:$userHistoryFlights")

        val insertPoint = dialogView.findViewById(R.id.main_view) as ViewGroup
        val title = insertPoint.findViewById(R.id.dialog_title) as TextView
//        if (x == 0)
//            title.text = "Twoje nadchodzące loty"
//        else
//            title.text = "Twoja historia lotów"

        title.text = ""
        var i = 0;
        for (flight in if (x == 0) userCurrentFlights else userHistoryFlights) {
            val rowView: View = inflater.inflate(R.layout.flight_row, null)

            val wylot_z = rowView.findViewById(R.id.wylot_z) as TextView
            val przesiadki = rowView.findViewById(R.id.przesiadki) as TextView
            val przylot_do = rowView.findViewById(R.id.przylot_do) as TextView
            val godzina = rowView.findViewById(R.id.godzina) as TextView
            val data_wylotu = rowView.findViewById(R.id.data_wylotu) as TextView
            val cena = rowView.findViewById(R.id.cena) as TextView
            val przewoznik = rowView.findViewById(R.id.przewoznik) as TextView

            wylot_z.text = flight[2]
            przesiadki.text = ""
            przylot_do.text = flight[1]
            godzina.text = flight[5]
            data_wylotu.text = flight[0]
            cena.text = flight[3]
            przewoznik.text = flight[4]


            insertPoint.addView(rowView, insertPoint.childCount)

            rowView.setOnClickListener {
                val intent = Intent(this, ProfileFlightsDetails::class.java)
                val seatValue = SeatValue(stringToArrayList(flight[7]))
                intent.putExtra("ids", seatValue)
                intent.putExtra("class", flight[6])
                intent.putExtra("wylot_z", flight[2])
                intent.putExtra("przylot_do", flight[1])
                intent.putExtra("data_wylotu", flight[0])
                intent.putExtra("cena", flight[3])
                intent.putExtra("originIata", flight[8])
                intent.putExtra("destinationIata", flight[9])
                setIntent(intent)
                startActivity(intent)
            }
            i++;
        }

//        dialogBuilder.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
//            return@OnClickListener
//        })
        dialogBuilder.setView(dialogView)

        val mAlertDialog = dialogBuilder.show()
    }

    fun createCurrentReservationList() {


        val r1 = findViewById<RelativeLayout>(R.id.reservation1)
        val r2 = findViewById<RelativeLayout>(R.id.reservation2)
        val btn = findViewById<Button>(R.id.buttonMoreCurrent)
        val findBtn = findViewById<Button>(R.id.findBtn)

        val txt1 = r1.findViewById(R.id.reservation1_txt1) as TextView
        val txt2 = r1.findViewById(R.id.reservation1_txt2) as TextView
        val txt3 = r1.findViewById(R.id.reservation1_txt3) as TextView

        val txt5 = r2.findViewById(R.id.reservation2_txt1) as TextView
        val txt6 = r2.findViewById(R.id.reservation2_txt2) as TextView
        val txt7 = r2.findViewById(R.id.reservation2_txt3) as TextView

        if (userCurrentFlights.size == 0) {
            println("no current reservations")

            r1.visibility = View.GONE;
            r2.visibility = View.GONE;
            btn.visibility = View.GONE;
            //r1.removeAllViews()
            //r2.removeAllViews()


            //(btn.getParent() as ViewManager).removeView(btn)
            btn.visibility = View.GONE;

            val txtEmpty1 = findViewById(R.id.emptyInfoCurrent) as TextView
            txtEmpty1.visibility = View.VISIBLE;


            findBtn.visibility = View.VISIBLE;
        } else {
            findBtn.visibility = View.GONE;
            if (userCurrentFlights.size == 1) {
                println("only one")
                r2.visibility = View.GONE;
                btn.visibility = View.GONE;

//                val documentArray = ArrayList<String>()
//                documentArray.add(document.data["date"] as String)
//                documentArray.add(document.data["dest_place"] as String)
//                documentArray.add(document.data["origin_place"] as String)
//                documentArray.add(document.data["price"] as String)
//                documentArray.add(document.data["carrier"] as String)
//                documentArray.add(document.data["hour"] as String)
//                documentArray.add(document.data["seatClass"] as String)
//                documentArray.add((document.data["seatArray"] as ArrayList<*>).toString())


                txt1.text = userCurrentFlights[0][2] //"Polska"
                txt2.text = userCurrentFlights[0][1]//"Kanada"
                txt3.text = userCurrentFlights[0][0]//"15.01.2021"

                r1.visibility = View.VISIBLE;
            } else if (userCurrentFlights.size >= 2) {
                if (userCurrentFlights.size > 2)
                    btn.visibility = View.VISIBLE;

                txt1.text = userCurrentFlights[0][2] //"Polska"
                txt2.text = userCurrentFlights[0][1]//"Kanada"
                txt3.text = userCurrentFlights[0][0]//"15.01.2021"


                txt5.text = userCurrentFlights[1][2] //"Polska"
                txt6.text = userCurrentFlights[1][1]//"Kanada"
                txt7.text = userCurrentFlights[1][0]//"15.01.2021"


                r1.visibility = View.VISIBLE;
                r2.visibility = View.VISIBLE;
            }
        }
        if (userCurrentFlights.size > 0) {
            r1.setOnClickListener {
                showFlightDetails(0, userCurrentFlights)
            }
            if (userCurrentFlights.size >= 2) {
                r2.setOnClickListener {
                    showFlightDetails(1, userCurrentFlights)
                }
            }
        }

    }

    fun createHistoryReservationList() {


        val r1 = findViewById(R.id.history1) as RelativeLayout
        val r2 = findViewById(R.id.history2) as RelativeLayout
        val btn = findViewById(R.id.buttonMoreHistory) as Button

        val txt1 = r1.findViewById(R.id.history1_txt1) as TextView
        val txt2 = r1.findViewById(R.id.history1_txt2) as TextView
        val txt3 = r1.findViewById(R.id.history1_txt3) as TextView

        val txt5 = r2.findViewById(R.id.history2_txt1) as TextView
        val txt6 = r2.findViewById(R.id.history2_txt2) as TextView
        val txt7 = r2.findViewById(R.id.history2_txt3) as TextView

        if (userHistoryFlights.size == 0) {
            println("no history reservations")

            r1.visibility = View.GONE
            r2.visibility = View.GONE
            //r1.removeAllViews()
            //r2.removeAllViews()


            //(btn.getParent() as ViewManager).removeView(btn)
            btn.visibility = View.GONE

            val txtEmpty1 = findViewById(R.id.emptyInfoHistory) as TextView
            txtEmpty1.visibility = View.VISIBLE;
        } else {
            if (userHistoryFlights.size == 1) {
                println("only one")
                r2.visibility = View.GONE
                btn.visibility = View.GONE


                txt1.text = userHistoryFlights[0][2] //"Polska"
                txt2.text = userHistoryFlights[0][1]//"Kanada"
                txt3.text = userHistoryFlights[0][0]//"15.01.2021"

                r1.visibility = View.VISIBLE;
            } else if (userHistoryFlights.size >= 2) {
                if (userHistoryFlights.size > 2)
                    btn.visibility = View.VISIBLE

                txt1.text = userHistoryFlights[0][2] //"Polska"
                txt2.text = userHistoryFlights[0][1]//"Kanada"
                txt3.text = userHistoryFlights[0][0]//"15.01.2021"


                txt5.text = userHistoryFlights[1][2] //"Polska"
                txt6.text = userHistoryFlights[1][1]//"Kanada"
                txt7.text = userHistoryFlights[1][0]//"15.01.2021"


                r1.visibility = View.VISIBLE
                r2.visibility = View.VISIBLE

            }
        }

        if (userCurrentFlights.size > 0) {
            r1.setOnClickListener {
                showFlightDetails(0, userHistoryFlights)
            }

            if (userCurrentFlights.size >= 2) {
                r2.setOnClickListener {
                    showFlightDetails(1, userHistoryFlights)
                }
            }
        }
    }


    private fun showFlightDetails(flightId: Int, flight: ArrayList<ArrayList<String>>) {
        val intent = Intent(this, ProfileFlightsDetails::class.java)
        val seatValue = SeatValue(stringToArrayList(flight[flightId][7]))
        intent.putExtra("ids", seatValue)
        intent.putExtra("class", flight[flightId][6])
        intent.putExtra("wylot_z", flight[flightId][2])
        intent.putExtra("przylot_do", flight[flightId][1])
        intent.putExtra("data_wylotu", flight[flightId][0])
        intent.putExtra("cena", flight[flightId][3])
        intent.putExtra("originIata", flight[flightId][8])
        intent.putExtra("destinationIata", flight[flightId][9])
        setIntent(intent)
        startActivity(intent)
    }

    private fun getUserFlights() {
        val uid = Firebase.auth.currentUser?.uid.toString()
//        val uid = "jyim5xqJsrQxB54Xr3w0IRHiA5r2"
        val db = Firebase.firestore


        val userDocRef = db.collection("users").document(uid)
        userDocRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val flightsHashes = task.result?.get("flights") as ArrayList<*>

                    val flightsDocRef = db.collection("flights")
                    flightsDocRef.get()
                        .addOnSuccessListener { documents ->
                            userCurrentFlights.clear()
                            userHistoryFlights.clear()
                            for (flight in flightsHashes) {
                                for (document in documents) {
                                    if (document.id == flight) {
                                        val documentArray = addFlightDataToArray(document)
                                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                                        val strDate: Date = sdf.parse(documentArray[0])
                                        if (Date().after(strDate))
                                            userHistoryFlights.add(documentArray)
                                        else
                                            userCurrentFlights.add(documentArray)
                                    }
                                }
                            }
                            println("Current: " + userCurrentFlights);
                            println("history: " + userHistoryFlights);

                            println("current size: " + userCurrentFlights.size)
                            println("history size: " + userHistoryFlights.size)

                            createCurrentReservationList()
                            createHistoryReservationList()

                        }
                        .addOnFailureListener { exception ->
                            Log.d(ProfileActivity.TAG, "get documents from flights collection failed with ", exception)
                        }
                } else {
                    println("no flights");
                    Log.d(ProfileActivity.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ProfileActivity.TAG, "get documents from user collection failed with ", exception)
            }
    }

    private fun addFlightDataToArray(document: QueryDocumentSnapshot): ArrayList<String> {
        val documentArray = ArrayList<String>()
        documentArray.add(document.data["date"] as String)
        documentArray.add(document.data["dest_place"] as String)
        documentArray.add(document.data["origin_place"] as String)
        documentArray.add(document.data["price"] as String)
        documentArray.add(document.data["carrier"] as String)
        documentArray.add(document.data["hour"] as String)
        documentArray.add(document.data["seatClass"] as String)
        documentArray.add(((document.data["seatArray"] as ArrayList<*>).toString()))
        documentArray.add(document.data["origin_iata"] as String)
        documentArray.add(document.data["dest_iata"] as String)

        return documentArray

    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            android.R.id.home -> {
                navigation_view.setCheckedItem(R.id.ic_profile);
                drawer.openDrawer(GravityCompat.START)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun stringToArrayList(text: String): ArrayList<String> {
        val seatsArrayString = text.substring(1, text.length - 1)
        println("lista siedzen: $seatsArrayString")
        val seatsArray: ArrayList<String>
        val newSeatsArray = ArrayList<String>()
        if (seatsArrayString.length > 3) {
            seatsArray = seatsArrayString.split(",") as ArrayList<String>
            for (seat in seatsArray) {
                newSeatsArray.add(seat.replace(" ", ""))
            }
        } else {
            newSeatsArray.add(seatsArrayString)
        }

        return newSeatsArray
    }


    companion object {
        const val TAG = "ProfileActivity"
    }
}
