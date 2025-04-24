package com.pettranslator.cattranslator.catsounds.ui.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pettranslator.cattranslator.catsounds.bases.recyclerview.BaseRecyclerViewAdapter
import com.pettranslator.cattranslator.catsounds.databinding.ItemLanguageBinding
import com.pettranslator.cattranslator.catsounds.model.LanguageItem

class LanguageAdapter : BaseRecyclerViewAdapter<LanguageItem, LanguageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLanguageBinding.inflate(layoutInflater, parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.language = item

        holder.binding.root.setOnClickListener {
            itemClickListener?.invoke(it,item,position)
            notifyDataSetChanged()
        }

        holder.binding.executePendingBindings()
    }
}

class LanguageViewHolder(val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root)
