package com.example.mrsooltest

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.mrsooltest.databinding.ActivityDetailsBinding
import com.example.mrsooltest.models.Billionaire
import com.example.mrsooltest.utils.Utils
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    lateinit var billionaire: Billionaire
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()


        val billionaireJson = intent.getStringExtra("billionaire")
        val gson = Gson()
        billionaire = gson.fromJson(billionaireJson, Billionaire::class.java)
        initViews()


    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        billionaire.rank?.apply {
            binding.tvRank.text = "#${this}"
        }
        Glide.with(this).load(billionaire.squareImage).placeholder(R.drawable.ic_menu_report_image)
            .into(binding.ivProfileImage)

        billionaire.personName?.apply {
            binding.tvPersonName.text = this
        }
        billionaire.birthDate?.apply {
            val age = Utils.getAge(this)
            val birthDate = Date(this)

            val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
            val formattedDate = formatter.format(birthDate)

            binding.tvBirth.text = "Born: $formattedDate ($age years)"
        }
        var countryString = ""
        if (billionaire.city != null) {
            countryString += "${billionaire.city},"
        }

        billionaire.countryOfCitizenship?.apply {
            binding.tvCountry.text = "$countryString $this"
        }
        billionaire.finalWorth?.apply {
            val netWorthInBillions = this / 1000
            val formattedNetWorth = String.format("%.2f", netWorthInBillions)
            binding.tvNetWorth.text = "Net-worth: \$$formattedNetWorth Billion"
        }

        billionaire.bios?.apply {
            var biosString = ""
            for (bio in this) {
                biosString += "$bio\n\n"
            }
            binding.labelBio.apply {
                isVisible = true
                text = "Bio: "
            }
            binding.tvBios.apply {
                text = biosString
                isVisible = true
            }
        }


        billionaire.abouts?.apply {
            var aboutsString = ""
            for (about in this) {
                aboutsString += "$about\n\n"
            }
            binding.tvAbouts.apply {
                text = aboutsString
                isVisible = true
            }
            binding.labelAbouts.apply {
                text = "About: "
                isVisible = true

            }


            billionaire.industries?.apply {
                var industriesString = ""
                for (industry in this) {
                    industriesString += "$industry\n\n"
                }
                binding.labelIndustries.isVisible = true
                binding.tvIndustries.apply {
                    isVisible = true
                    text = industriesString
                }
            }

            billionaire.financialAssets?.apply {
                var financialAssetsString = ""
                for (asset in this) {

                    financialAssetsString += "Name: ${asset.companyName}\nShares: ${asset.numberOfShares!!.toInt()}\nShare price: \$${
                        String.format(
                            "%.2f",
                            asset.sharePrice
                        )
                    }\n\n"
                }
                binding.labelFinancialAssets.isVisible = true
                binding.tvFinancialAssets.apply {
                    isVisible = true
                    text = financialAssetsString
                }

            }
            binding.tvLink.apply {
                val url = "https://www.forbes.com/profile/${billionaire.uri}"
                text = url
                setOnClickListener {
                    openInBrowser(url)

                }

            }
        }


    }

    private fun openInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)


        }
    }
}