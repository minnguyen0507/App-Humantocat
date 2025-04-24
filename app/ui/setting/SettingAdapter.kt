package com.pettranslator.cattranslator.catsounds.ui.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pettranslator.cattranslator.catsounds.bases.recyclerview.BaseRecyclerViewAdapter
import com.pettranslator.cattranslator.catsounds.databinding.ItemSettingBinding
import com.pettranslator.cattranslator.catsounds.model.Animal

class SettingAdapter : BaseRecyclerViewAdapter<Animal, SettingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSettingBinding.inflate(layoutInflater, parent, false)
        return SettingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.animal = item

        holder.binding.root.setOnClickListener {
            itemClickListener?.invoke(it,item,position)
        }


        holder.binding.executePendingBindings()
    }
}

class SettingViewHolder(val binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root)