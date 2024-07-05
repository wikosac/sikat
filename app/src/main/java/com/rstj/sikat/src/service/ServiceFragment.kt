package com.rstj.sikat.src.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rstj.sikat.databinding.FragmentServiceBinding

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val histories = listOf(
            HistoryModel(title = "Terminal - Banjaran", date = "Sabtu, 6 Juli 2024 • 14:37"),
            HistoryModel(title = "Halte SMP 6 - Terminal Tegal", date = "Kamis, 4 Juli 2024 • 08:12"),
            HistoryModel(title = "Slawi - Terminal Tegal", date = "Selasa, 2 Juli 2024 • 15:23"),
            HistoryModel(title = "Terminal Tegal - Slawi", date = "Selasa, 2 Juli 2024 • 08:04"),
            HistoryModel(title = "Halte Yos Sudarso - Halte Mayjend", date = "Senin, 1 Juli 2024 • 08:07"),
        )

        binding.rvHistory.apply {
            adapter = HistoryAdapter(histories)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}