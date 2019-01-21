package com.chungo.base.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

open class BasePagerAdapter constructor(fragmentManager: FragmentManager, list: List<Fragment>, titles: Array<CharSequence>) : FragmentStatePagerAdapter(fragmentManager) {
    var mList: List<Fragment>? = list
    var mTitles: Array<CharSequence>? = titles

    override fun getItem(position: Int): Fragment? {
        return mList?.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (mTitles != null && position < mTitles!!.size) {
            mTitles!![position]
        } else super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return if (mList == null) 0 else mList!!.size
    }
}
