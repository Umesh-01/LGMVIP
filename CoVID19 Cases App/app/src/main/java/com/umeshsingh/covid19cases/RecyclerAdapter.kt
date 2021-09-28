package com.umeshsingh.covid19cases

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(context: Context, val valueList: ArrayList<Model>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapter.RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_single_row, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.RecyclerViewHolder, position: Int) {
        val covidInfo = valueList[position]
        holder.nameCity.text = covidInfo.cityName
        holder.caseTotal.text = covidInfo.totalCase.toString()
//        holder.caseActive.text = covidInfo.activeCase.toString()
        holder.caseRec.text = covidInfo.recCase.toString()
        holder.caseDeath.text = covidInfo.deathCase.toString()

    }

    override fun getItemCount(): Int {
        return valueList.size
    }

    class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view){
        val nameCity: TextView = view.findViewById(R.id.txtCityName)
        val caseTotal: TextView = view.findViewById(R.id.txtTotalCases)
//        val caseActive: TextView = view.findViewById(R.id.txtActiveCases)
        val caseRec: TextView = view.findViewById(R.id.txtRecCases)
        val caseDeath: TextView = view.findViewById(R.id.txtDeathCases)

    }

}
