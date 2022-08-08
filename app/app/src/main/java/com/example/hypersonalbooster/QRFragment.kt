package com.example.hypersonalbooster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hypersonalbooster.databinding.FragmentQrBinding

class QRFragment : Fragment() {
    lateinit var binding : FragmentQrBinding

    override fun onCreateView(inflater: LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        binding = FragmentQrBinding.inflate(inflater, container, false)

        binding.close.setOnClickListener {
            val fragmentTransaction = getActivity()?.getSupportFragmentManager()?.beginTransaction()
            fragmentTransaction?.replace(R.id.test, RegisterFragment2_1())
            fragmentTransaction?.commit()
        }
        return binding.root
    }
}