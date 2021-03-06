package com.example.dzivekodywallet.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.data.database.model.Operation
import com.example.dzivekodywallet.data.util.CopyOnClickListener
import com.example.dzivekodywallet.databinding.ItemOperationBinding

class TransactionDetailsAdapter(
    private val mList: ArrayList<Operation> = ArrayList(),
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

            binding.operationItemOpIdButton.setOnClickListener(CopyOnClickListener(
                binding.root.context,
                "Operation ID",
                operation.operationId.toString()
            ))
            binding.operationItemOpDstAccButton.setOnClickListener(CopyOnClickListener(
                binding.root.context,
                "Destination Account of Operation",
                operation.destinationAccount
            ))

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

