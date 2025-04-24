package com.pettranslator.cattranslator.catsounds.ui.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pettranslator.cattranslator.catsounds.databinding.ItemIntroBinding
import com.pettranslator.cattranslator.catsounds.model.IntroSlide

class IntroAdapter(
    private val items: List<IntroSlide>
) : RecyclerView.Adapter<IntroAdapter.SlideViewHolder>() {

    inner class SlideViewHolder(val binding: ItemIntroBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val binding = ItemIntroBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val slide = items[position]
        with(holder.binding) {
            imgSlide.setImageResource(slide.imageRes)
            tvTitle.text = slide.title
            tvDescription.text = slide.description
        }
    }

    override fun getItemCount(): Int = items.size
}

