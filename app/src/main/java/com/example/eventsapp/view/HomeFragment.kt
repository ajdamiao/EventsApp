package com.example.eventsapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventsapp.databinding.FragmentHomeBinding
import com.example.eventsapp.model.Event
import com.example.eventsapp.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.eventsapp.MainActivity
import com.example.eventsapp.R
import com.example.eventsapp.view.adapter.EventsAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private var homeViewModel = HomeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        homeViewModel.getEvents()
        eventsResponse()

        val main = activity as MainActivity
        main.setToolbarTitle("Eventos")
    }

    private fun setupCard(response: ArrayList<Event>) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = EventsAdapter(response)
    }

    private fun eventsResponse() {
        homeViewModel.getEventReponse().observe(viewLifecycleOwner, { response ->
            when(response) {
                is ArrayList<Event> -> {
                    println(response)
                    setupCard(response) }

                else -> errorDialog()
            }
        })
    }

    private fun errorDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogTheme)
                .setTitle("Erro!")
                .setMessage("Não foi possível carregar os eventos.")
                .setNegativeButton("FECHAR") { dialogInterface, _ ->
                    Navigation.findNavController(requireView()).popBackStack() }
                .show()
        }
    }
}