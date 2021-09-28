package com.umeshsingh.covid19cases

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    lateinit var countryTotal: TextView
    lateinit var countryRec: TextView
    lateinit var countryDeath: TextView


    lateinit var worldTotal: TextView
    lateinit var worldRec: TextView
    lateinit var worldDeath: TextView

    lateinit var recyclerView: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: RecyclerAdapter


    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val covidList = arrayListOf<Model>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        countryTotal = findViewById(R.id.txtTotalCasesCountry)
        countryRec = findViewById(R.id.txtRecCasesCountry)
        countryDeath = findViewById(R.id.txtDeathCasesCountry)

        worldTotal = findViewById(R.id.txtTotalCasesWorld)
        worldRec = findViewById(R.id.txtRecCasesWorld)
        worldDeath = findViewById(R.id.txtDeathCasesWorld)

        recyclerView = findViewById(R.id.RecyclerView)

        progressLayout = findViewById(R.id.ProgressLayout)
        progressBar = findViewById(R.id.ProgressBar)
        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(this)

        getWorldCovidInfo()

        getStateCovidInfo()

    }


    private fun getStateCovidInfo() {

        val queue = Volley.newRequestQueue(applicationContext)

        val url = "https://api.rootnet.in/covid19-in/stats/latest"

        if (ConnectionManager().checkConnectivity((this))) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener() {
                    //println("Response is $it")

                    try {
                        progressLayout.visibility = View.GONE

                        val dataObject = it.getJSONObject("data")
                        val summaryObject = dataObject.getJSONObject("summary")

                        val cases: Int = summaryObject.getInt("total")
                        val recovered: Int = summaryObject.getInt("discharged")
                        val deaths: Int = summaryObject.getInt("deaths")

                        countryTotal.text = cases.toString()
                        countryRec.text = recovered.toString()
                        countryDeath.text = deaths.toString()

                        val regionalArray = dataObject.getJSONArray("regional")


                        //                val listIterator = up.listIterator()

                        //                while(listIterator.hasNext()) {
                        //
                        //                    val indexString = data2.getJSONObject(listIterator.next()).toString()


                        for (i in 0 until regionalArray.length()) {
                            val regionalObj = regionalArray.getJSONObject(i)
                            val stateName: String = regionalObj.getString("loc")
                            val cases: Int = regionalObj.getInt("totalConfirmed")
                            val deaths: Int = regionalObj.getInt("deaths")
                            val recovered: Int = regionalObj.getInt("discharged")


                            val objectModel = Model(
                                stateName, 0, cases, deaths, recovered
                            )

                            covidList.add(objectModel)

                            recyclerAdapter = RecyclerAdapter(this, covidList)

                            recyclerView.adapter = recyclerAdapter

                            recyclerView.layoutManager = layoutManager
                            // }
                        }

                    } catch (c: JSONException) {
                        Toast.makeText(this, "Some unexpected error occurred!!", Toast.LENGTH_SHORT)
                            .show()
                    }


                }, Response.ErrorListener {
                    Toast.makeText(
                        this,
                        "Volley error occurred!!",
                        Toast.LENGTH_SHORT
                    ).show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"

                        return headers
                    }

                }
            queue.add(jsonObjectRequest)

        } else {

            val dialog = android.app.AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                //Opening setting using implicit intent
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                this?.finish()
            }

            dialog.setNegativeButton("Exit") { text, listener ->
                //Closing the app
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()

        }

    }

    private fun getWorldCovidInfo() {
        val url = "https://corona.lmao.ninja/v3/covid-19/all"
        val queue = Volley.newRequestQueue(this)
        val request =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    val totalCasesWorld: Int = response.getInt("cases")
                    val recoveredWorld: Int = response.getInt("recovered")
                    val deathWorld: Int = response.getInt("deaths")

                    worldTotal.text = totalCasesWorld.toString()
                    worldRec.text = recoveredWorld.toString()
                    worldDeath.text = deathWorld.toString()

                } catch (e: JSONException) {
                    Toast.makeText(this, "Some unexpected error occurred!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }, { error ->
                Toast.makeText(
                    this,
                    "Volley error occurred!!",
                    Toast.LENGTH_SHORT
                ).show()

            })
        queue.add(request)

    }
}