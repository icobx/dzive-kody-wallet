package com.example.dzivekodywallet.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.viewmodel.WalletItemViewModel

class WalletItemAdapter(private val mList: List<Wallet>?) : RecyclerView.Adapter<WalletItemAdapter.ViewHolder>() {
//    interface WalletClick {
//        fun onClick(wallet: Wallet) {}
//    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wallet, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wallet = mList?.get(position)
        // sets the image to the imageview from our itemHolder class

        // sets the text to the textview from our itemHolder class
        if (wallet != null) {
            holder.textView.text = wallet.name
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        if (mList != null) {
            return mList.size
        }
        return 0
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.wallet_item_text_view)
    }
}