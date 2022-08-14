package com.example.hypersonalbooster

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hypersonalbooster.databinding.FragmentMapRequestSearchBinding


class KioskFragment_Detail_Search() : DialogFragment() {

    lateinit var binding: FragmentMapRequestSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapRequestSearchBinding.inflate(inflater, container, false)

        val view = binding.root
        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //취소 버튼 동작
        binding.cancel.setOnClickListener {
            dismiss()
        }

        //검색 버튼 동작
        binding.boosterSearch.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}




