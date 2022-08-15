package com.example.hypersonalbooster

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hypersonalbooster.databinding.FragmentMapDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class KioskFragment_Detail() : BottomSheetDialogFragment() {

    lateinit var binding : FragmentMapDetailBinding
    var star : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMapDetailBinding.inflate(inflater, container, false)

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.request.setOnClickListener{
            val detailSearchDialog = KioskFragment_Detail_Search()
            detailSearchDialog.show(parentFragmentManager, detailSearchDialog.tag)
        }

        binding.favorite.setOnClickListener{
            if(star == 0){
                binding.star.setImageResource(R.drawable.icon_star_main_color)
                binding.favorite.text = "즐겨찾기 해제"
                star = 1
            }
            else if(star == 1){
                binding.star.setImageResource(R.drawable.icon_star_gray)
                binding.favorite.text = "즐겨찾기 등록"
                star = 0
            }
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
        return getWindowHeight() * 50 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}

