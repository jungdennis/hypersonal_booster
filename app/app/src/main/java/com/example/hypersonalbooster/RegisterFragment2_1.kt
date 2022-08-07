package com.example.hypersonalbooster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hypersonalbooster.databinding.FragmentRegisterInbodyYesBinding


class RegisterFragment2_1 : Fragment() {
    lateinit var binding : FragmentRegisterInbodyYesBinding

    lateinit var fat : String
    lateinit var muscle : String

    override fun onCreateView(inflater: LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        binding = FragmentRegisterInbodyYesBinding.inflate(inflater, container, false)

        fat = binding.insertFat.text.toString()
        muscle = binding.insertMuscle.text.toString()

        return binding.root
    }
}