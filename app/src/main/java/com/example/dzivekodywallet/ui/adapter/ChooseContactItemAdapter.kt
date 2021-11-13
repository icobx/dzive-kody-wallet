package com.example.dzivekodywallet.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.data.database.AppDatabase
import com.example.dzivekodywallet.data.database.model.Contact
import com.example.dzivekodywallet.databinding.ItemContactBinding
import com.example.dzivekodywallet.databinding.ItemContactEditableBinding

class ChooseContactItemAdapter(private val mList: List<Contact>, private val clickListener: ChooseContactItemListener) : ListAdapter<Contact, ChooseContactItemAdapter.ViewHolder>(ChooseContactItemDiffCallback()) {

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
    class ViewHolder private constructor(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, clickListener: ChooseContactItemListener)
        {
            binding.contact = contact
            binding.contactItemTextView.text = contact.name
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContactBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ChooseContactItemDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.contactId == newItem.contactId
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

class ChooseContactItemListener(val clickListener: (contact: Contact) -> Unit) {
    fun onChooseContactItemClick(contact: Contact) {
        clickListener(contact)
//        contact?.let( clickListener() )
    }
}