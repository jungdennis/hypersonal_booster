package com.example.hypersonalbooster

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.example.hypersonalbooster.databinding.FragmentBoosterDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BoosterFragment(booster_info : String) : BottomSheetDialogFragment() {

    val booster_info_string = booster_info

    lateinit var binding : FragmentBoosterDetailBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val shared = context.getSharedPreferences("data_cloud", 0)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentBoosterDetailBinding.inflate(inflater, container, false)

        val booster = booster_info_string.split("*")

        val id = booster[0]
        val name = booster[1]
        val company = booster[2]
        val taste = booster[3]
        val texture = booster[4]
        val class_2 = booster[7]
        val amount = booster[8]
        val calories = booster[9]
        val carb = booster[10]
        val sugar = booster[11]
        val fat = booster[12]
        val sat_fat = booster[13]
        val protein = booster[14]
        val link = booster[15]

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.boosterName.text = name

        binding.boosterMaker.text = company

        binding.boosterFlavor.text = taste

        binding.infoAmount.text = "(1회 제공량 " + amount + " 기준)"

        binding.dataCalories.text = calories + "kcal"
        binding.dataCarb.text = carb + "g"
        binding.dataSugar.text = sugar + "g"
        binding.dataFat.text = fat + "g"
        binding.dataSatFat.text = sat_fat + "g"
        binding.dataProtein.text = protein + "g"

        var booster_url_name = ""

        if(name.contains(":")) {
            booster_url_name += name.replace(":", "").replace("%", "%25").replace(" ", "%20").replace("+","%2B")
        }
        else {
            booster_url_name += name.replace("%", "%25").replace(" ", "%20").replace("+","%2B")
        }

        val url = "https://firebasestorage.googleapis.com/v0/b/hypersonal-booster.appspot.com/o/" + booster_url_name + ".jpg?alt=media"
        Log.d("ListViewAdapter_Main", "Url : $url")

        binding.boosterImage.load(url) {
            placeholder(R.drawable.icon_loading)
            error(R.drawable.img_scoop)
        }

        binding.findVendor.setOnClickListener{
            val intent = Intent(getActivity(), KioskActivity::class.java)
            startActivity(intent)
        }
        binding.buyBooster.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
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