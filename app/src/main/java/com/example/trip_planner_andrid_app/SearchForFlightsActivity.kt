package com.example.trip_planner_andrid_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.core.view.GravityCompat
import com.example.trip_planner_andrid_app.flights.FlightsListActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_for_flights_activity.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.search_for_flights_activity.drawer
import java.text.SimpleDateFormat
import java.util.*


class SearchForFlightsActivity : AppCompatActivity() {

    private val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val formatText = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

    private var outboundDateString : String = ""
    private var inboundDateString : String = ""

    private var auth: FirebaseAuth? = null
    private var user: FirebaseUser? = null

    private var userName: String? = null
    private var userLastname: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_for_flights_activity)

        auth = Firebase.auth
        user = auth?.currentUser

        setupNavBar()
        if (user != null) {
            getUserName()
        }
        setAutocomplete()

        covidGo.setOnClickListener {
            startActivity(Intent(this, MapActivity()::class.java))
        }

        barcelonacard.setOnClickListener {
            val intent = Intent(this, FlightsListActivity::class.java)
            val originPlace = "PL-sky"
            val destinationPlace = "BCN-sky"
            val date = "2021-02"
            intent.putExtra("originPlace", originPlace)
            intent.putExtra("destinationPlace", destinationPlace)
            intent.putExtra("outboundDateString", date)
            intent.putExtra("inboundDateString", "")

            if (isNetworkAvailable(this)) {
                setIntent(intent)
                startActivity(intent)
            }
            else Toast.makeText(this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show()
        }

        button2.setOnClickListener{
            if (two_way.isChecked) {
                if (wylotZ.text.isEmpty()  || przylotDo.text.isEmpty() || inboundDateString.isEmpty() || outboundDateString.isEmpty() ) {
                    Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else {
                if (wylotZ.text.isEmpty()  || przylotDo.text.isEmpty() || outboundDateString.isEmpty() ) {
                    Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val originPlace = wylotZ.text.toString().substring(wylotZ.text.toString().lastIndexOf("-")+1).trim() + "-sky"
            val destinationPlace = przylotDo.text.toString().substring(przylotDo.text.toString().lastIndexOf("-")+1).trim() + "-sky"
            val intent = Intent(this, FlightsListActivity::class.java)

            intent.putExtra("originPlace", originPlace)
            intent.putExtra("destinationPlace", destinationPlace)
            if (two_way.isChecked) {
                intent.putExtra("inboundDateString", inboundDateString)
            }
            else {
                intent.putExtra("inboundDateString", "")
            }
            intent.putExtra("outboundDateString", outboundDateString)

            if (isNetworkAvailable(this)) {
                setIntent(intent)
                startActivity(intent)
            }
            else Toast.makeText(this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show()
        }

        var oneWay = true
        data_przylotu.visibility = View.INVISIBLE
        przylotText.visibility = View.INVISIBLE

        one_way.setOnClickListener {
            oneWay = true
            data_przylotu.visibility = View.INVISIBLE
            przylotText.visibility = View.INVISIBLE
        }

        two_way.setOnClickListener {
            oneWay = false
            data_przylotu.visibility = View.VISIBLE
            przylotText.visibility = View.VISIBLE
        }

        btn_datePicker.setOnClickListener {
            closeKeyboard()
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            val currentYear = calendar.get(Calendar.YEAR)

            calendar.set(Calendar.YEAR, currentYear)
            val startYear = calendar.timeInMillis

            calendar.set(Calendar.YEAR, currentYear + 2)
            val endYear = calendar.timeInMillis

            if (oneWay) {
                println(oneWay)
                val builder = MaterialDatePicker.Builder.datePicker()
                val constraintsBuilderRange = CalendarConstraints.Builder().setStart(startYear).setEnd(endYear)
                val dateValidator = DateValidatorPointForward.now()
                constraintsBuilderRange.setValidator(dateValidator)
                builder.setCalendarConstraints(constraintsBuilderRange.build())
                builder.setTitleText("Lot w jedną stronę")
                val picker: MaterialDatePicker<Long> = builder.build()
                picker.addOnPositiveButtonClickListener { selection ->
                    outboundDateString = formatDate.format(selection)
                    data_wylotu.text = formatText.format(selection)
                }
                picker.show(supportFragmentManager, picker.toString())

            } else {
                println(oneWay)
                val builder: MaterialDatePicker.Builder<Pair<Long, Long>> = MaterialDatePicker.Builder.dateRangePicker()
                val constraintsBuilderRange = CalendarConstraints.Builder().setStart(startYear).setEnd(endYear)
                val dateValidator = DateValidatorPointForward.now()
                constraintsBuilderRange.setValidator(dateValidator)
                builder.setCalendarConstraints(constraintsBuilderRange.build())
                builder.setTitleText("Lot w obie strony")
                val picker: MaterialDatePicker<Pair<Long, Long>> = builder.build()
                picker.addOnPositiveButtonClickListener { selection ->
                    val startDate = selection.first
                    val endDate = selection.second
                    outboundDateString = formatDate.format(startDate)
                    inboundDateString = formatDate.format(endDate)
                    data_wylotu.text = formatText.format(startDate)
                    data_przylotu.text = " - " + formatText.format(endDate)
                }
                picker.show(supportFragmentManager, picker.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        auth = Firebase.auth
        user = auth?.currentUser

        if (user != null) {
            getUserName()
        }
    }

    private fun getUserName() {
        val uid = user?.uid.toString()
        val db = Firebase.firestore

        val docRef = db.collection("users").document(uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userName = document["name"] as String?
                    userLastname = document["lastname"] as String?
                    textUsername.text = userName
                    findViewById<TextView>(R.id.navFullName)?.text = "$userName $userLastname"
                    findViewById<TextView>(R.id.navMail)?.text = user?.email
                } else {
                    println("brak dokumentu obecnego usera")
                }
            }
    }

    private fun setupNavBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navigation_view.setCheckedItem(R.id.nav_home);
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.ic_profile -> {
                    drawer.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, ProfileActivity()::class.java))
                }
                R.id.nav_map -> {
                    drawer.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, MapActivity()::class.java))
                }
                R.id.nav_tools -> {
                    drawer.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, MainActivity()::class.java))
                }
                R.id.nav_send -> {
                    drawer.closeDrawer(GravityCompat.START)
                    auth?.signOut()
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

    private fun setAutocomplete() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "data.json")
        val jsonFileString2 = getJsonDataFromAsset(applicationContext, "countries.json")

        val airports =  GsonBuilder().create().fromJson(jsonFileString, AirportGson.airportsList::class.java)
        val countries =  GsonBuilder().create().fromJson(jsonFileString2, CountriesGson.countriesList::class.java)
        val autocompleteList = ArrayList<String>()

        for(c in countries.countries) {
            autocompleteList.add(c.country + ", wszystkie lotniska - " + c.alfa_2)
        }

        for(a in airports.airports) {
            autocompleteList.add(a.city + " " + a.name + " - " + a.iata)
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line, autocompleteList
        )

        val textView = findViewById<AutoCompleteTextView>(R.id.wylotZ)
        val textView2 = findViewById<AutoCompleteTextView>(R.id.przylotDo)

        textView.setAdapter(adapter)
        textView2.setAdapter(adapter)
    }

    private fun getJsonDataFromAsset(context: Context, filename: String): String? {
        return context.assets.open(filename).bufferedReader().use { it.readText() }

    }

    private fun isNetworkAvailable(context : Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
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
                navigation_view.setCheckedItem(R.id.nav_home);
                drawer.openDrawer(GravityCompat.START)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}


