package com.pettranslator.cattranslator.catsounds.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pettranslator.cattranslator.catsounds.bases.recyclerview.BaseRecyclerViewAdapter
import com.pettranslator.cattranslator.catsounds.databinding.ItemAnimalSoundBinding
import com.pettranslator.cattranslator.catsounds.model.Animal

class AnimalAdapter : BaseRecyclerViewAdapter<Animal, AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAnimalSoundBinding.inflate(layoutInflater, parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.animal = item

        holder.binding.root.setOnClickListener {
            itemClickListener?.invoke(it,item,position)

        }

        holder.binding.executePendingBindings()
    }

}

class AnimalViewHolder(val binding: ItemAnimalSoundBinding) : RecyclerView.ViewHolder(binding.root)
