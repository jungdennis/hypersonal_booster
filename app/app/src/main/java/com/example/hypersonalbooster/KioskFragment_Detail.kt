package com.example.hypersonalbooster

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
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

    lateinit var kioskBooster_data : String
    lateinit var markerPos_data : String
    lateinit var database_data : String
    var kioskName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val prefs = requireActivity().getSharedPreferences("data_kioskData", Context.MODE_PRIVATE)
        kioskBooster_data = prefs.getString("kioskData", "failed").toString()

        val prefs2 = requireActivity().getSharedPreferences("data_markerPos", Context.MODE_PRIVATE)
        markerPos_data = prefs2.getString("markerPos", "failed").toString()

        binding = FragmentMapDetailBinding.inflate(inflater, container, false)


        var cnt = 0
        var kioskNum:Int = 0
        var mKioskArr = Array(3) { arrayOfNulls<String>(7) }
        var mm : Int = 0
        var x : Int = 0

        if (kioskBooster_data != null) {
            for (i in 0 until kioskBooster_data.length - 2) {
                if (kioskBooster_data!![i].toString() == "/") {
                    cnt++
                }
                kioskNum = cnt + 1
            }
            var n = 0
            var m = 0
            var kioskArr = Array(kioskNum) { arrayOfNulls<String>(7) }

            for (i in kioskBooster_data.indices) {
                if (kioskBooster_data!![i].toString() == ",") {
                    kioskArr[m][n] = kioskName
                    n++
                    kioskName = ""
                } else if (kioskBooster_data!![i].toString() == "/") {
                    kioskArr[m][n] = kioskName
                    n = 0
                    m++
                    kioskName = ""
                } else {
                    kioskName += kioskBooster_data!![i].toString()
                }
            }
            kioskArr[m][n] = kioskName


            kioskName = ""
            mKioskArr = kioskArr
            mm = m
        }

        var nameNum: Int = 0
        for(i in 0..mm){
            if(markerPos_data == mKioskArr[i][0]){
                nameNum = i
            }
        }
        binding.locationName.text = mKioskArr[nameNum][0]
        binding.b1.text = mKioskArr[nameNum][1]
        binding.b2.text = mKioskArr[nameNum][2]
        binding.b3.text = mKioskArr[nameNum][3]
        binding.b4.text = mKioskArr[nameNum][4]
        binding.b5.text = mKioskArr[nameNum][5]
        binding.b6.text = mKioskArr[nameNum][6]

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.request.setOnClickListener{
            activity?.let{
                val intent = Intent (it, KioskRequestRecommand::class.java)
                it.startActivity(intent)
            }
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

