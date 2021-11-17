package com.example.dzivekodywallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dzivekodywallet.R
import com.example.dzivekodywallet.data.util.Injection
import com.example.dzivekodywallet.databinding.FragmentSendReceiveBinding
import com.example.dzivekodywallet.ui.adapter.ChooseContactItemAdapter
import com.example.dzivekodywallet.ui.adapter.ChooseContactItemListener
import com.example.dzivekodywallet.viewmodel.SendReceiveViewModel
import com.example.dzivekodywallet.viewmodel.WalletViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import java.lang.Exception


class SendReceiveFragment : Fragment() {
    private lateinit var binding: FragmentSendReceiveBinding
    private lateinit var srViewModel: SendReceiveViewModel
    private lateinit var wViewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendReceiveBinding.inflate(
            inflater,
            container,
            false
        )

        srViewModel = ViewModelProvider(
            requireActivity(),
            Injection.provideSendReceiveViewModelFactory(requireContext())
        )[SendReceiveViewModel::class.java]

        wViewModel = ViewModelProvider(
            requireActivity()
        )[WalletViewModel::class.java]

//        requireActivity().onBackPressedDispatcher.addCallback(
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    Log.d("in callback", "in callback")
//                    Navigation.findNavController(binding.root)
//                        .popBackStack(R.id.wallet_man_activity, false)
//                }
//            }
//        )
        binding.sendReceiveSendButton.setOnClickListener {
            wViewModel.makeTransaction(
                binding.sendRecEditTextPubk.text.toString(),
                binding.sendReceiveEditTextAmount.text.toString()
            )
        }

        val imageView = binding.qrCode
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(
                "Public key placeholder",
                BarcodeFormat.QR_CODE,
                500,
                500
            )
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendReceiveContactButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.dialog_choose_contact,null)

            // Buttons
            val closeButton = dialogView.findViewById<ImageView>(R.id.dialog_choose_contact_close)

            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            dialogView.findViewById<RecyclerView>(R.id.dialog_choose_contact_recycler_view).layoutManager = LinearLayoutManager(requireContext())

            srViewModel.getContacts().observe(viewLifecycleOwner, Observer { contacts ->
                dialogView.findViewById<RecyclerView>(R.id.dialog_choose_contact_recycler_view).adapter = ChooseContactItemAdapter(contacts, ChooseContactItemListener { contact ->
                    binding.sendRecEditTextPubk.setText(contact.pubKey)
                    dialog.dismiss()
                })
            })

            dialog.setContentView(dialogView)
            dialog.show()
        }

        Log.d("onViewCreated", "in send/receive fragment")
    }

}