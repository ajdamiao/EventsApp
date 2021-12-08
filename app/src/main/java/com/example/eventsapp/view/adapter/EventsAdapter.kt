package com.example.eventsapp.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventsapp.R
import com.example.eventsapp.databinding.RviewEventsBinding
import com.example.eventsapp.model.Event
import java.util.*
import kotlin.collections.ArrayList

class EventsAdapter(private val events: ArrayList<Event>) : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {
    inner class EventsViewHolder(val binding: RviewEventsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.EventsViewHolder {
        val binding = RviewEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventsAdapter.EventsViewHolder, position: Int) {
        with(holder) {
            with(events[position]) {

                val c = Calendar.getInstance()
                c.timeInMillis = date

                val date = "${c[Calendar.DAY_OF_MONTH]}/${c[Calendar.MONTH]}/${c[Calendar.YEAR]}"

                binding.txtEventName.text = title
                binding.txtEventDate.text = date
                binding.txtEventPrice.text = "R$ $price"

                Glide.with(holder.itemView)
                    .load(image)
                    .placeholder(R.drawable.ic_page_not_found)
                    .into(binding.eventImage)

                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("id", id)
                    Navigation.findNavController(itemView).navigate(R.id.eventDetailsFragment, bundle)
                }
            }
        }
    }

    override fun getItemCount() = events.size
}