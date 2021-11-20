package com.example.dzivekodywallet.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Transaction
import com.example.dzivekodywallet.databinding.ItemTransactionBinding

class TransactionsAdapter(
    private val mList: ArrayList<Transaction> = ArrayList()
): RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

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

    fun setTransactions(transactions: List<Transaction>) {
        val diffResult = DiffUtil.calculateDiff(TransactionDiffCallback(this.mList, transactions))
        this.mList.clear()
        this.mList.addAll(transactions)
        diffResult.dispatchUpdatesTo(this)
    }

    // Holds the views for adding it to image and text
    class ViewHolder private constructor(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction)
        {
            binding.transaction = transaction
            Log.d("JFLOG", "IN transAdapter.bind: $transaction")

            val isSuccessfulResource = if (transaction.successful) R.drawable.ic_check_circle_outline_24 else R.drawable.ic_close_24
            binding.transactionItemIsSuccessfulImageView.setImageResource(isSuccessfulResource)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTransactionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TransactionDiffCallback(
    private val oldList: List<Transaction>,
    private val newList: List<Transaction>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].transactionId == newList[newItemPosition].transactionId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}