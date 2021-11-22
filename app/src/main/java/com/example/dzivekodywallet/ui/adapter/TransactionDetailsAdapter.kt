package com.example.dzivekodywallet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.data.database.model.Operation
import com.example.dzivekodywallet.databinding.ItemOperationBinding

class TransactionDetailsAdapter(
    private val mList: ArrayList<Operation> = ArrayList()
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

    fun setOperations(operations: List<Operation>) {
        val diffResult = DiffUtil.calculateDiff(OperationDiffCallback(this.mList, operations))
        this.mList.clear()
        this.mList.addAll(operations)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder private constructor(val binding: ItemOperationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(operation: Operation) {
            binding.operation = operation

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
    private val oldList: List<Operation>,
    private val newList: List<Operation>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].operationId == newList[newItemPosition].operationId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
