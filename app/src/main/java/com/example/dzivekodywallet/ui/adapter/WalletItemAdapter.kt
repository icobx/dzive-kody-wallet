package com.example.dzivekodywallet.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.viewmodel.WalletItemViewModel
import com.example.dzivekodywallet.databinding.ItemWalletBinding

class WalletItemAdapter(private val mList: List<Wallet>, private val clickListener: WalletItemListener) : ListAdapter<Wallet, WalletItemAdapter.ViewHolder>(WalletItemDiffCallback()) {

    // create new views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder private constructor(val binding: ItemWalletBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wallet: Wallet, clickListener: WalletItemListener)
        {
            binding.wallet = wallet
            binding.walletItemTextView.text = wallet.name
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemWalletBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class WalletItemDiffCallback : DiffUtil.ItemCallback<Wallet>() {

    override fun areItemsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
        return oldItem.walletId == newItem.walletId
    }

    override fun areContentsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
        return oldItem == newItem
    }
}

class WalletItemListener(val clickListener: (walletId: Long) -> Unit ) {

    fun onWalletItemClick(wallet: Wallet) {
        Log.d("FANCYCLICKLISTENER", wallet.name)
        wallet.walletId?.let { clickListener(it) }
    }

    fun onWalletItemDeleteClick(wallet: Wallet) {
        Log.d("FANCYCLICKLISTENER", "Delete: ${wallet.name}")
    }
}