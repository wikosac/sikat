package com.rstj.sikat.src.maps

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rstj.sikat.R
import com.rstj.sikat.databinding.BottomSheetBinding

class BottomSheetFragment(
    private val routeTitle: String,
    private val transitsTitle: List<String>
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
    }

    companion object {
        const val TAG = "YourBottomSheetDialogFragment"

        fun newInstance(routeTitle: String, transitsTitle: List<String>): BottomSheetFragment {
            return BottomSheetFragment(routeTitle, transitsTitle)
        }
    }
}
