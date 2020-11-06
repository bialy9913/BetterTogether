package com.example.bettertogether.ui.my_account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.bettertogether.R
import androidx.recyclerview.widget.RecyclerView
import com.example.bettertogether.databinding.RecyclerviewCurrOffersBinding
import com.example.bettertogether.models.CurrOffer


class CurrOffersAdapter(
    private val offers:List<CurrOffer>
    ,private val listener: MyAccountViewModel
): RecyclerView.Adapter<CurrOffersAdapter.CurrOffersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CurrOffersViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_curr_offers,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = offers.size

    override fun onBindViewHolder(holder: CurrOffersViewHolder, position: Int) {
        holder.recycleViewCurrOffersBinding.currOffer= offers[position]
        holder.recycleViewCurrOffersBinding.myAccountViewModel=listener
    }

    inner class CurrOffersViewHolder(
        val recycleViewCurrOffersBinding: RecyclerviewCurrOffersBinding
    ) : RecyclerView.ViewHolder(recycleViewCurrOffersBinding.root)
}