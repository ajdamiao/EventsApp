package com.example.eventsapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private lateinit var binding: FragmentSplashBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSplashBinding.bind(view)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val textAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_appear_animation)
        binding.txtAppName.startAnimation(textAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            openHome()
        }, 3000)
    }

    private fun openHome() {
        view?.let { Navigation.findNavController(it).navigate(R.id.homeFragment) }
    }
}