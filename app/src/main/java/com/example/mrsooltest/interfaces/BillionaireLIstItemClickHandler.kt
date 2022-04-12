package com.example.mrsooltest.interfaces

import com.example.mrsooltest.models.Billionaire

interface BillionaireLIstItemClickHandler {
    fun onItemClicked(position: Int, billionaire: Billionaire)
}