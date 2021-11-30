package com.example.dzivekodywallet.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Operation
import com.example.dzivekodywallet.data.util.CopyOnClickListener
import com.example.dzivekodywallet.databinding.ItemOperationBinding
import com.example.dzivekodywallet.databinding.ItemPaymentBinding

class PaymentsAdapter(
    private val payments: ArrayList<Operation> = ArrayList(),
): RecyclerView.Adapter<PaymentsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(payments[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    fun setPayments(operations: List<Operation>) {
        val diffResult = DiffUtil.calculateDiff(OperationDiffCallback(this.payments, operations))
        this.payments.clear()
        this.payments.addAll(operations)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder private constructor(val binding: ItemPaymentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(payment: Operation) {
            binding.payment = payment

            val isReceivedResource =
                if (payment.isReceived!!)
                R.drawable.ic_received_32 else
                R.drawable.ic_sent_32

            binding.paymentItemIcon.setImageResource(isReceivedResource)

            binding.paymentIdDstSrcButton.setOnClickListener(CopyOnClickListener(
                binding.root.context,
                "Operation ID",
                if (payment.isReceived!!) payment.sourceAccount  else payment.destinationAccount
            ))
            val buttonColor = binding.root.context.resources.getColor(
                if (payment.isReceived!!) R.color.colorButtonReceived else R.color.colorButtonSent,
                null
            )
            binding.paymentIdDstSrcButton.setBackgroundColor(buttonColor)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater =  LayoutInflater.from(parent.context)
                val binding = ItemPaymentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


