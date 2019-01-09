package com.chungo.baseapp.widget.refresh

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.util.DensityUtil

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/5/22 15:29
 */
class PrizeRefresh @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SmartRefreshLayout(context, attrs, defStyleAttr) {
    private var mTipsSpinner = DensityUtil.dp2px(32f)
    private var mTips: TipsView? = null

    private fun initView() {
        val parent = this.parent
        if (parent is FrameLayout) {
            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)
                if (child is TipsView) {
                    mTipsSpinner = DensityUtil.dp2px(32f)
                    mTips = child
                    mTips!!.btnView!!.setOnClickListener {
                        autoRefresh()
                        mTips!!.hide()
                    }
                }
            }
        }
    }

    fun finishRefresh(tips: String): SmartRefreshLayout {
        showTips(tips)
        return finishRefresh(1000)
    }

    public override fun resetStatus() {
        super.resetStatus()
    }

    private fun showTips(tips: String) {
        CheackNull()
        Log.d("hcg", "isActivated()=$isActivated")
        //moveSpinnerInfinitely(mTipsSpinner);
        mTips!!.show(tips)
        postDelayed({ mTips!!.hide(250) }, 1000)
    }

    /**
     * 关闭提示
     */
    fun hideTips() {
        CheackNull()
        animSpinner(0, 100, mReboundInterpolator, mReboundDuration)
        mTips!!.hide()
    }

    override fun getRefreshHeader(): RefreshHeader? {
        return super.getRefreshHeader()
    }

    /**
     * 展示提示按钮
     *
     * @param text
     */
    fun showTipsButton(text: String) {
        CheackNull()
        refreshHeader!!.view.visibility = View.INVISIBLE
        animSpinner(mTipsSpinner, 100, mReboundInterpolator, mReboundDuration)
        mTips!!.showButton(text)
        postDelayed({
            mTips!!.hide(500)
            animSpinner(0, 0, mReboundInterpolator, mReboundDuration)
        }, 3000)
    }

    private fun CheackNull() {
        if (mTips == null)
            initView()
        val height = mTips!!.height
        if (mTipsSpinner != height)
            mTipsSpinner = mTips!!.height
    }
}
