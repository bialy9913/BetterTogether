package com.example.bettertogether.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bettertogether.R
import com.example.bettertogether.databinding.RecyclerviewOffersBinding
import com.example.bettertogether.ui.offers.OffersNavigator

class OffersAdapter(
    private val offers:List<Offer>
    ,private val listener:OffersNavigator
):RecyclerView.Adapter<OffersAdapter.OffersViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OffersViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_offers,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = offers.size

    override fun onBindViewHolder(holder: OffersViewHolder, position: Int) {
        holder.recycleViewOffersBinding.offer= offers[position]
        holder.recycleViewOffersBinding.btnChooseOffer.setOnClickListener{
            listener.userChoosedOffer(offers[position])
        }
    }

    inner class OffersViewHolder(
        val recycleViewOffersBinding:RecyclerviewOffersBinding
    ) : RecyclerView.ViewHolder(recycleViewOffersBinding.root)
}