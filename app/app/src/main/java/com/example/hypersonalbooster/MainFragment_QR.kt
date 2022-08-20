package com.example.hypersonalbooster

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.hypersonalbooster.databinding.FragmentQrBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MainFragment_QR() : BottomSheetDialogFragment() {

    lateinit var binding : FragmentQrBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentQrBinding.inflate(inflater, container, false)

        binding.close.setOnClickListener {
            dismiss()
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

    /*
    private fun createQRCode(){
        val qrCode = QRCodeWriter()
        val bitMtx = qrCode.encode(
            intent.getStringExtra("id"),
            BarcodeFormat.QR_CODE,
            350,
            350
        )
        val bitmap: Bitmap = Bitmap.createBitmap(bitMtx.width, bitMtx.height, Bitmap.Config.RGB_565)
        for(i in 0 .. bitMtx.width-1){
            for(j in 0 .. bitMtx.height-1){
                var color = 0
                if(bitMtx.get(i, j)){
                    color = Color.BLACK
                }else{
                    color = Color.WHITE
                }
                bitmap.setPixel(i, j, color)
            }
        }
        binding.qrImage.setImageBitmap(bitmap)
    }

     */

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog){
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams

        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 70 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}