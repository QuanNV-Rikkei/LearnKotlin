package com.rikkei.tra_02t0115browser.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    private val listFragment: MutableList<Fragment> = mutableListOf()
    private val listTitle: MutableList<String> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        listFragment.add(fragment)
        listTitle.add(title)
        notifyDataSetChanged()
    }

    fun removeFragment(position: Int) {
        listFragment.removeAt(position)
        listTitle.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        if (position < count) {
            val manager = (`object` as Fragment).fragmentManager
            val trans = manager!!.beginTransaction()
            trans.remove(`object`)
            trans.commit()
        }
    }

    override fun finishUpdate(container: ViewGroup) {
        super.finishUpdate(container)
    }

    /*override fun destroyItem(container: View, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        removeFragment(position)
    }*/

}