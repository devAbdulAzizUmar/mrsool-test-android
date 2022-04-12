package com.example.mrsooltest.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mrsooltest.R
import com.example.mrsooltest.databinding.ListItemBillionaireBinding
import com.example.mrsooltest.interfaces.BillionaireLIstItemClickHandler
import com.example.mrsooltest.models.Billionaire
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


const val TAG = "BillionairesAdapter"

class BillionairesAdapter(val context: Context) :
    RecyclerView.Adapter<BillionairesAdapter.BillionaireViewHolder>() {
    var listItemClickHandler: BillionaireLIstItemClickHandler? = null

    var isInit = true

    inner class BillionaireViewHolder(val binding: ListItemBillionaireBinding) :
        RecyclerView.ViewHolder(binding.root)

    var billionaires: List<Billionaire> = listOf()
        get() = field
        set(value) {
            field = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillionaireViewHolder {
        return BillionaireViewHolder(
            ListItemBillionaireBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BillionaireViewHolder, position: Int) {
        listItemClickHandler?.apply {
            holder.binding.root.setOnClickListener {
                this.onItemClicked(position, billionaires[position])
            }
        }

        holder.binding.apply {
            val billionaire = billionaires[position]

            if (isInit) {
                isInit = false
                Log.d(TAG, "onBindViewHolder: ${billionaire.birthDate}")
            }

            billionaire.apply {
                if (personName != null) {
                    tvPersonName.text = billionaire.personName
                } else {
                    tvPersonName.visibility = View.GONE
                }

                if (person?.squareImage != null) {
                    Glide.with(holder.itemView).load(person.squareImage)
                        .placeholder(R.mipmap.ic_launcher)
                        .fitCenter()
                        .into(ivAvatar)
                }

                if (rank != null) {
                    tvRank.text = "#${rank}"
                } else {
                    tvRank.isGone = true
                }

                if (birthDate != null) {
                    val birthDate = Date(birthDate)
                    val currentDate = Calendar.getInstance().time
                    val difference = currentDate.time - birthDate.time
                    val seconds = difference / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val days = hours / 24
                    val years = days / 365
                    tvAge.text = "Age: $years"
                }
                if (countryOfCitizenship != null) {
                    tvCountry.text = "Country: $countryOfCitizenship"
                }
                if (finalWorth != null) {
                    val netWorthInBillions = finalWorth / 1000

                    tvNetworth.text = "$${String.format("%.2f", netWorthInBillions)} B"
                }

                source?.apply {
                    tvSource.text = this
                }
            }

        }
    }

    override fun getItemCount() = billionaires.size


}