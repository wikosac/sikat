package com.rstj.sikat.src.auth.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rstj.sikat.MainActivity
import com.rstj.sikat.R
import com.rstj.sikat.databinding.FragmentLoginBinding
import com.rstj.sikat.src.Resource
import com.rstj.sikat.src.ViewModelFactory
import com.rstj.sikat.src.auth.AuthViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val navController by lazy { findNavController() }
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val viewModel: AuthViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnKirim.setOnClickListener { onSubmit() }
        binding.tvDaftar.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun onSubmit() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()
        viewModel.login(email, password).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.loadingIndicator.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    viewModel.saveTokenPref(email)

                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()

                    Toast.makeText(requireContext(), "Berhasil Masuk", Toast.LENGTH_SHORT).show()
                    binding.loadingIndicator.visibility = View.GONE
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.exception.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.loadingIndicator.visibility = View.GONE
                }
            }
        }
    }

}