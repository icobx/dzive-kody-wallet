package com.example.dzivekodywallet

import androidx.biometric.BiometricPrompt
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.dzivekodywallet.databinding.FragmentAuthBinding
import java.util.concurrent.Executor


class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    // Leave empty for now.
    // View initialization logic
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(
            inflater,
            container,
            false
        )

        binding.textView = "tu sa bude davat pin"


        executor = ContextCompat.getMainExecutor(this.requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(activity,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(activity,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_authFragment_to_walletsFragment)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(activity, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })


        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL or BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .build()

        binding.correctPinButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        return binding.root
    }

    // Post view initialization logic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("onViewCreated", "ANO")
        // ...and so on
    }

}
