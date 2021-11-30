package com.example.dzivekodywallet.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.dzivekodywallet.data.database.model.Operation

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