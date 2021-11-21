package com.example.dzivekodywallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.databinding.ItemOperationBinding

class TransactionDetailsAdapter(
    private val mList: ArrayList<String> = ArrayList()
): RecyclerView.Adapter<TransactionDetailsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setOperations(operations: List<String>) {
        val diffResult = DiffUtil.calculateDiff(OperationDiffCallback(this.mList, operations))
        this.mList.clear()
        this.mList.addAll(operations)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder private constructor(val binding: ItemOperationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(something: String) {
            binding.amount = 5000.0
            binding.operationType = something
            binding.destinationAccount = "Patrik"

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater =  LayoutInflater.from(parent.context)
                val binding = ItemOperationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class OperationDiffCallback(
    private val oldList: List<String>,
    private val newList: List<String>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}