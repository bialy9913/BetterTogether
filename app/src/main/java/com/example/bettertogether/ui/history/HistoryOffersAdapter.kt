package com.example.bettertogether.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bettertogether.R
import com.example.bettertogether.databinding.RecyclerviewHistoryOffersBinding
import com.example.bettertogether.databinding.RecyclerviewOffersBinding
import com.example.bettertogether.models.CurrOffer
import com.example.bettertogether.models.Offer

class HistoryOffersAdapter(
    private val offers:List<CurrOffer>
    ,private val listener:HistoryViewModel
):RecyclerView.Adapter<HistoryOffersAdapter.OffersViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OffersViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_history_offers,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = offers.size

    override fun onBindViewHolder(holder: OffersViewHolder, position: Int) {
        holder.recycleViewHistoryOffersBinding.currOffer= offers[position]
    }

    inner class OffersViewHolder(
        val recycleViewHistoryOffersBinding: RecyclerviewHistoryOffersBinding
    ) : RecyclerView.ViewHolder(recycleViewHistoryOffersBinding.root)
}