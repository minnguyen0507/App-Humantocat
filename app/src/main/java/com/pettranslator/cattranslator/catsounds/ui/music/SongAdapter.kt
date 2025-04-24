package com.pettranslator.cattranslator.catsounds.ui.music

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pettranslator.cattranslator.catsounds.bases.recyclerview.BaseRecyclerViewAdapter
import com.pettranslator.cattranslator.catsounds.databinding.ItemAnimalSongBinding
import com.pettranslator.cattranslator.catsounds.model.Animal
import com.pettranslator.cattranslator.catsounds.model.Song

class SongAdapter : BaseRecyclerViewAdapter<Song, SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAnimalSongBinding.inflate(layoutInflater, parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.binding.song = item

        holder.binding.layoutSong.setOnClickListener {
            itemClickListener?.invoke(it,item,position)
        }

        holder.binding.imvStar.setOnClickListener {
            itemClickListener?.invoke(it,item,position)
        }

        holder.binding.executePendingBindings()
    }
}

class SongViewHolder(val binding: ItemAnimalSongBinding) : RecyclerView.ViewHolder(binding.root)