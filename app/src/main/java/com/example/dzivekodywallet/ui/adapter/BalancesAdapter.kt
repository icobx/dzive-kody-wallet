package com.example.dzivekodywallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.data.database.model.Balance
import com.example.dzivekodywallet.databinding.ItemBalanceBinding




class BalancesAdapter(
    private val mList: ArrayList<Balance> = ArrayList()
): RecyclerView.Adapter<BalancesAdapter.ViewHolder>() {

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

    fun setBalances(balances: List<Balance>) {
        val diffResult = DiffUtil.calculateDiff(BalanceDiffCallback(this.mList, balances))
        this.mList.clear()
        this.mList.addAll(balances)
        diffResult.dispatchUpdatesTo(this)
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

class BalanceDiffCallback(
    private val oldList: List<Balance>,
    private val newList: List<Balance>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].balanceId == newList[newItemPosition].balanceId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
