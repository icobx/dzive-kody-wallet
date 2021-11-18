package com.example.dzivekodywallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.databinding.ItemBalanceBinding

class BalancesAdapter(private val mList: List<Balance>): ListAdapter<Balance, BalancesAdapter.ViewHolder>(BalanceItemDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder private constructor(val binding: ItemBalanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(balance: Balance)
        {
            binding.singleBalanceAmount.text = balance.amount.toString()
            binding.assetName = balance.assetName
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBalanceBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class BalanceItemDiffCallback : DiffUtil.ItemCallback<Balance>() {
    override fun areItemsTheSame(oldItem: Balance, newItem: Balance): Boolean {
        return oldItem.balanceId == newItem.balanceId
    }

    override fun areContentsTheSame(oldItem: Balance, newItem: Balance): Boolean {
        return oldItem.amount == newItem.amount
                && oldItem.assetName == newItem.assetName
                && oldItem.walletId == newItem.walletId
    }

}
