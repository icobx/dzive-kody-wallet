package com.example.dzivekodywallet.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.data.database.AppDatabase
import com.example.dzivekodywallet.data.database.model.Contact
import com.example.dzivekodywallet.databinding.ItemContactEditableBinding

class ContactItemAdapter(private val mList: List<Contact>, private val clickListener: ContactItemListener) : ListAdapter<Contact, ContactItemAdapter.ViewHolder>(ContactItemDiffCallback()) {

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
    class ViewHolder private constructor(val binding: ItemContactEditableBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, clickListener: ContactItemListener)
        {
            binding.contact = contact
            binding.contactItemTextView.text = contact.name
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContactEditableBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ContactItemDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.contactId == newItem.contactId
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

class ContactItemListener(val clickListener: (contactId: Long) -> Unit, val deleteClickListener: (contact: Contact) -> Unit ) {
    fun onContactItemDeleteClick(contact: Contact) {
        Log.d("FANCYCLICKLISTENER", "Delete: ${contact.name}")
        contact?.let { deleteClickListener(it) }
    }

    fun onContactItemEditClick(contact: Contact) {

//        contact?.let( editClickListener(it) )
    }
}