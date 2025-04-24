package com.pettranslator.cattranslator.catsounds.bases

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager,  lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = mutableListOf<() -> Fragment>()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].invoke()
    }

    fun add(index: Int, fragmentCreator: () -> Fragment) {
        fragments.add(index, fragmentCreator)
        notifyItemInserted(index)
    }

    fun replace(index: Int, fragmentCreator: () -> Fragment) {
        fragments[index] = fragmentCreator
        notifyItemChanged(index)
    }

    fun remove(index: Int) {
        fragments.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.find { it.hashCode().toLong() == itemId } != null
    }
}