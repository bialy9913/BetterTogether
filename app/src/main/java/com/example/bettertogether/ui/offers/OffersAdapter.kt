package com.example.bettertogether.ui.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bettertogether.R
import com.example.bettertogether.databinding.RecyclerviewOffersBinding
import com.example.bettertogether.models.Offer

class OffersAdapter(
    private val offers:List<Offer>
    ,private val listener:OffersViewModel
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
            listener.userChosedOffer(offers[position])
        }
    }

    inner class OffersViewHolder(
        val recycleViewOffersBinding:RecyclerviewOffersBinding
    ) : RecyclerView.ViewHolder(recycleViewOffersBinding.root)
}