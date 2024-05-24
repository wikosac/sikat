package com.rstj.sikat.src.auth.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.rstj.sikat.MainActivity
import com.rstj.sikat.R
import com.rstj.sikat.databinding.FragmentRegisterBinding
import com.rstj.sikat.src.auth.AuthViewModel
import com.rstj.sikat.src.utils.Resource
import com.rstj.sikat.src.utils.ViewModelFactory

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val navController by lazy { findNavController() }
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val viewModel: AuthViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnKirim.setOnClickListener { onSubmit() }
        binding.tvMasuk.setOnClickListener {
            navController.navigate(
                R.id.action_registerFragment_to_loginFragment,
                null,
                navOptions {
                    popUpTo(R.id.loginFragment) { inclusive = true }
                }
            )
        }
    }

    private fun onSubmit() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        viewModel.register(email, password).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    // Show loading indicator
                }
                is Resource.Success -> {
                    viewModel.saveTokenPref(email)

                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()

                    Toast.makeText(requireContext(), "Berhasil Daftar", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}