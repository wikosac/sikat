package com.rstj.sikat.src.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rstj.sikat.databinding.FragmentProfileBinding
import com.rstj.sikat.src.auth.AuthActivity
import com.rstj.sikat.src.auth.AuthViewModel
import com.rstj.sikat.src.utils.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(requireContext()) }
    private val viewModel: AuthViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTokenPref().observe(viewLifecycleOwner) {
            if (it != null) binding.tvEmailUser.text = it
        }

        binding.btnLogout.setOnClickListener { onLogoutClicked() }
    }

    private fun onLogoutClicked() {
        AlertDialog.Builder(requireContext())
            .setTitle("Peringatan")
            .setMessage("Apa kamu yakin untuk Logout?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.deleteTokenPref()
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
                Toast.makeText(requireContext(), "Berhasil logout!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}