package com.rstj.sikat.src.maps

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rstj.sikat.databinding.BottomSheetBinding

class BottomSheetFragment(
    private val routeTitle: String,
    private val transitsTitle: List<TransitModel>,
    private val listener: TransitAdapter.OnTransitItemClickListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setDimAmount(0f)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitleSheet.text = routeTitle
        binding.rvTransit.apply {
            adapter = TransitAdapter(transitsTitle, listener)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        const val TAG = "YourBottomSheetDialogFragment"

        private var instance: BottomSheetFragment? = null

        fun newInstance(
            routeTitle: String,
            transitsTitle: List<TransitModel>,
            listener: TransitAdapter.OnTransitItemClickListener
        ): BottomSheetFragment? {
            return if (instance == null) {
                instance = BottomSheetFragment(routeTitle, transitsTitle, listener)
                instance!!
            } else {
                null
            }
        }

        fun clearInstance() {
            instance = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearInstance()
    }
}
