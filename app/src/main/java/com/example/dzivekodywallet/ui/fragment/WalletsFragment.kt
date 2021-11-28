package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.database.model.Wallet
import com.example.dzivekodywallet.viewmodel.WalletsViewModel
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentWalletsBinding
import com.example.dzivekodywallet.ui.adapter.WalletItemAdapter
import com.example.dzivekodywallet.ui.adapter.WalletItemListener
import com.google.android.material.bottomsheet.BottomSheetDialog

class WalletsFragment : Fragment() {
    private lateinit var binding: FragmentWalletsBinding
    private lateinit var viewModel: WalletsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletsBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideWalletsViewModelFactory(requireContext())
        )[WalletsViewModel::class.java]

        binding.walletsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getWallets().observe(viewLifecycleOwner, Observer { wallets ->
            binding.walletsRecyclerView.adapter = WalletItemAdapter(wallets, WalletItemListener(
                { walletId ->
                    viewModel.updateWalletStatus(walletId)
                    findNavController().navigate(WalletsFragmentDirections.actionWalletsFragmentToWalletFragment(walletId))
                },
                { wallet ->
                    viewModel.deleteWallet(wallet)
                },
                { wallet ->
                    requestNewWalletName(wallet) { viewModel.updateWallet(wallet) }
                }
            )
            )
        })

        viewModel.error.observe(viewLifecycleOwner, { error ->
            if (error != null) {
                Log.d("ERROR", error.toString())
//                (binding.balancesRecyclerView.adapter as BalancesAdapter)
//                    .setBalances(walletBalances)
            }
        })

        return binding.root
    }

    private fun addExistingWallet() {
        findNavController().navigate(WalletsFragmentDirections.actionWalletsFragmentToAddWalletFragment2())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addExistingButton =binding.addWalletImageButton

        addExistingButton.setOnClickListener {
            addExistingWallet()
        }
    }

    private fun requestNewWalletName(wallet: Wallet, handler: (wallet: Wallet) -> Unit) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_wallet_name,null)
        dialog.setContentView(dialogView)
        val closeButton = dialogView.findViewById<ImageView>(R.id.dialog_edit_wallet_name_close)
        val newName = dialogView.findViewById<EditText>(R.id.dialog_edit_wallet_name_new_name)
        val saveButton = dialogView.findViewById<Button>(R.id.dialog_edit_wallet_save_button)
        val errorMessage = dialogView.findViewById<TextView>(R.id.dialog_edit_wallet_name_error)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        errorMessage.visibility = View.GONE

        saveButton.setOnClickListener {
            wallet.name = newName.text.toString()

            if (wallet.name.isNotEmpty()) {
                errorMessage.visibility = View.GONE
                handler(wallet)
                dialog.dismiss()
            } else {
                errorMessage.visibility = View.VISIBLE
            }
        }

        dialog.show()
    }
}