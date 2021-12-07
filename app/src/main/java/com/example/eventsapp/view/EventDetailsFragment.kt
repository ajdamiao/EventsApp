package com.example.eventsapp.view

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentEventDetailsBinding
import com.example.eventsapp.exception.CustomException
import com.example.eventsapp.model.CheckIn
import com.example.eventsapp.model.EventDetail
import com.example.eventsapp.util.Util
import com.example.eventsapp.viewmodel.EventDetailViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class EventDetailsFragment : Fragment(R.layout.fragment_event_details) {
    private lateinit var binding: FragmentEventDetailsBinding
    private val eventDetailsViewModel : EventDetailViewModel by viewModels()
    private var popupInputDialogView: View? = null
    private val util = Util()
    private var eventId = String()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEventDetailsBinding.bind(view)
        eventId = requireArguments().getString("id").toString()

        eventDetailsViewModel.getEventDetail(eventId)

        eventResponse()
        checkInResponse()

        binding.btnShareEvent.setOnClickListener {
            shareFunction()
        }

        binding.btnCheckIn.setOnClickListener {
            checkInAlertDialog()
        }
    }

    private fun checkInAlertDialog()
    {
        val layoutInflater = LayoutInflater.from(requireContext())
        popupInputDialogView = layoutInflater.inflate(R.layout.checkin_alert_dialog, null)
        showCheckInDialog()
    }

    private fun showCheckInDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.checkInAlertDialog)
                .setView(popupInputDialogView)
                .setPositiveButton("CANCELAR") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .setNegativeButton("CONFIRMAR") { _, _ ->

                    val name: EditText = (popupInputDialogView.let { popupInputDialogView?.findViewById<View>(R.id.inputName) as EditText })
                    val email: EditText = (popupInputDialogView.let { popupInputDialogView?.findViewById<View>(R.id.inputEmail) as EditText })

                    if(util.isEmailValid(email.text.toString())) {
                        if (util.isNameValid(name.text.toString()))  {
                            val postInfo = CheckIn(eventId, name.text.toString(), email.text.toString())
                            eventDetailsViewModel.checkInEvent(postInfo)
                        }
                        else {
                            invalidFieldDialog("Todos os campos devem ser preenchidos.")
                        }
                    }
                    else {
                        invalidFieldDialog("É preciso colocar um email valido.")
                    }
                    hideKeyboard()
                }.show()
        }
    }

    private fun invalidFieldDialog(mensagem: String) {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogThemeBenefit)
                .setTitle("Erro")
                .setMessage(mensagem)
                .setPositiveButton("OK"){ DialogInterface, _ ->
                    DialogInterface.dismiss()
                }
        }?.show()
    }

    private fun eventResponse() {

        eventDetailsViewModel.getEventDetailResponse().observe(viewLifecycleOwner, { response ->
            when(response) {

                is EventDetail -> setupEventInfo(response)

                else ->  errorDialog()
            }
        })
    }

    private fun setupEventInfo(response: EventDetail) {
        binding.progressBar.visibility = View.GONE
        binding.layoutDetails.visibility = View.VISIBLE
        binding.eventDetailsImage.visibility = View.VISIBLE

        val geocoder = Geocoder(requireContext())
        val addresses = geocoder.getFromLocation(response.latitude, response.longitude, 1)

        val c = Calendar.getInstance()
        c.timeInMillis = response.date

        val date = "${c[Calendar.DAY_OF_MONTH]}/${c[Calendar.MONTH]}/${c[Calendar.YEAR]} | ${c[Calendar.HOUR_OF_DAY]}:${c[Calendar.MINUTE]}${c[Calendar.SECOND]}"

        binding.txtEventDateAndTime.text = date
        binding.txtEventTitle.text = response.title
        binding.txtAboutEvent.text = response.description
        binding.txtEventPrice.text = "${response.price}"
        binding.txtEventLocation.text = addresses[0].getAddressLine(0)

        Glide.with(requireView())
            .load(response.image)
            .placeholder(R.drawable.ic_page_not_found)
            .into(binding.eventDetailsImage)
    }

    private fun errorDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogTheme)
                .setTitle("Erro!")
                .setMessage("Não foi possivel carregar informações do evento.")
                .setNegativeButton("FECHAR") { dialogInterface, _ ->
                    Navigation.findNavController(requireView()).popBackStack() }
                .show()
        }
    }

    private fun checkInResponse() {
        eventDetailsViewModel.getEventCheckInResponse().observe(viewLifecycleOwner, { response ->
            when(response) {
                is Boolean -> {
                    if (response) {
                        dialogConfirmCheckIn()
                    }
                    else {
                        dialogErrorCheckIn()
                    }
                }

                is CustomException -> {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao fazer check-in, tente novamente",
                        Toast.LENGTH_LONG
                    ).show()
                    println("entrou response exception")
                }
            }
        })
    }

    private fun dialogConfirmCheckIn()
    {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogThemeBenefit)
                .setTitle("Sucesso")
                .setMessage("Check-in realizado com sucesso.")
                .setNegativeButton("OK"){ DialogInterface, _ ->
                    Navigation.findNavController(requireView())
                        .navigate(R.id.homeFragment)
                }
        }?.show()
    }

    private fun dialogErrorCheckIn()
    {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialogThemeBenefit)
                .setTitle("Erro")
                .setMessage("Erro ao fazer Check-in.")
                .setNegativeButton("OK"){ DialogInterface, _ ->
                    DialogInterface.dismiss()
                }
        }?.show()
    }


    private fun shareFunction() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        startActivity(Intent.createChooser(sharingIntent, "Share using"))
    }

    private fun hideKeyboard() {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }
}