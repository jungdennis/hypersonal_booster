package com.example.hypersonalbooster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hypersonalbooster.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var binding : FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val shared = getActivity()?.getSharedPreferences("data_health", 0)
        var height = shared?.getFloat("height", 0F)
        var weight = shared?.getFloat("weight", 0F)
        var fat = shared?.getFloat("fat", 0F)
        var muscle = shared?.getFloat("muscle", 0F)
        var nickname = shared?.getString("nickname", "닉네임없음")
        binding.heightDisplay.text = height.toString()
        binding.weightDisplay.text = weight.toString()
        binding.displayFat.text = fat.toString()
        binding.displayMuscle.text = muscle.toString()
        binding.userName.text = nickname

        return binding.root
    }
}