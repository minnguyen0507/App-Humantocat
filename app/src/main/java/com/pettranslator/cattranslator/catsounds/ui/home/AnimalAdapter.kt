package com.pettranslator.cattranslator.catsounds.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pettranslator.cattranslator.catsounds.bases.recyclerview.BaseRecyclerViewAdapter
import com.pettranslator.cattranslator.catsounds.databinding.ItemAnimalSoundBinding
import com.pettranslator.cattranslator.catsounds.model.Animal
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener

class AnimalAdapter : BaseRecyclerViewAdapter<Animal, AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAnimalSoundBinding.inflate(layoutInflater, parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.animal = item

        holder.binding.root.setSafeOnClickListener(1500) {
            itemClickListener?.invoke(it,item,position)
        }

        holder.binding.executePendingBindings()
    }

}

class AnimalViewHolder(val binding: ItemAnimalSoundBinding) : RecyclerView.ViewHolder(binding.root)
