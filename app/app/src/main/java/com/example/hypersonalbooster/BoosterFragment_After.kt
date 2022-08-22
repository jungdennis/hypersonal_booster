package com.example.hypersonalbooster

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hypersonalbooster.databinding.FragmentBoosterDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BoosterFragment_After() : BottomSheetDialogFragment() {

    lateinit var binding : FragmentBoosterDetailBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val shared = context.getSharedPreferences("data_cloud", 0)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentBoosterDetailBinding.inflate(inflater, container, false)

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.findVendor.setOnClickListener{
            val intent = Intent(getActivity(), KioskActivity::class.java)
            startActivity(intent)
        }
        binding.buyBooster.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.naver.com"))
            startActivity(intent)
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // val dialog = super.onCreateDialog(savedInstanceState)
        val dialog = BottomSheetDialog(requireContext(), R.style.bottom_dialog)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }

        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog){
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams

        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 80 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}