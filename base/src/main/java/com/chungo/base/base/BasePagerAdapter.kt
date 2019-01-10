package com.chungo.base.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class BasePagerAdapter : FragmentStatePagerAdapter {
    private var mList: List<Fragment>? = null
    private var mTitles: Array<CharSequence>? = null

    @JvmOverloads
    constructor(fragmentManager: FragmentManager, list: List<Fragment>, titles: Array<CharSequence>) : super(fragmentManager) {
        this.mList = list
        this.mTitles = titles
    }

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
