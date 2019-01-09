package com.chungo.baseapp.widget.refresh

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import com.chungo.baseapp.R
import com.scwang.smartrefresh.layout.util.DensityUtil

/**
 * @Description
 * @Author huangchangguo
 * @Created 2018/5/22 15:29
 */
class TipsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    private val TAG = "hcg_" + this.javaClass.simpleName
    private var mCtx: Context? = null

    private var mTipsBackgroundColor: Int = 0
    private var mTipsTextColor: Int = 0
    private var mTipsTextSize: Int = 0
    private var mTipsText: String? = null

    private var mTextView: TextView? = null
    var btnView: TextView? = null
        private set

    private var isTVShown = false
    private var isTipsBtnShown = false
    private var mHiddenAction: TranslateAnimation? = null
    private var mShowAction: TranslateAnimation? = null

    private val mTvStartAnimDuration = 800// 提示条展示的动画间隔时间
    private val mBtnStartAnimDuration = 500// 按钮展示的动画间隔时间
    private val mShowAnimDuration = 1 * 1000// 提示条展示的时间

    private val mEndAnimDuration = 200// 提示条结束的动画间隔时间

    private var mAnimatorSet: AnimatorSet? = null

    init {
        init(context, attrs)
    }

    companion object {
        internal val SCALE_X = "scaleX"
        internal val SCALE_Y = "scaleY"
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        this.mCtx = context

        val tyArray = context.obtainStyledAttributes(attrs,
                R.styleable.TipsView)
        mTipsBackgroundColor = tyArray.getColor(
                R.styleable.TipsView_TipsBackgroundColor,
                0X33000000)
        mTipsTextColor = tyArray.getColor(
                R.styleable.TipsView_TipsTextColor,
                Color.parseColor("#B2ffffff"))
        mTipsTextSize = tyArray.getDimension(
                R.styleable.TipsView_TipsTextSize, 12f).toInt()
        mTipsText = tyArray
                .getString(R.styleable.TipsView_TipsText)
        tyArray.recycle()

        gravity = Gravity.CENTER

        mTextView = TextView(mCtx)
        mTextView!!.text = mTipsText
        mTextView!!.setTextColor(mTipsTextColor)
        //mTextView.setTextColor(0xffffffff);
        mTextView!!.textSize = mTipsTextSize.toFloat()

        val textViewParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT)

        btnView = TextView(mCtx)
        // mButton.setHeight(LayoutParams.WRAP_CONTENT);
        // mButton.setWidth(LayoutParams.WRAP_CONTENT);
        btnView!!.background = mCtx!!.getDrawable(R.drawable.bg_tips_selector)
        btnView!!.setTextColor(Color.parseColor("#3da8f2"))
        btnView!!.textSize = mTipsTextSize.toFloat()
        btnView!!.gravity = Gravity.CENTER
        btnView!!.setShadowLayer(0f, 1f, 1f, Color.parseColor("#e8e8e8"))
        // int padding = DensityUtil.dp2px(10);
        val padding = DensityUtil.dp2px(10f)
        btnView!!.setPadding(padding, 0, padding, 0)
        val layoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)
        layoutParams.topMargin = 15
        layoutParams.bottomMargin = 0

        addView(mTextView, textViewParams)
        addView(btnView, layoutParams)

        //		View lines = new View(mCtx);
        //		RelativeLayout.LayoutParams linesParams = new RelativeLayout.LayoutParams(
        //				LayoutParams.MATCH_PARENT, 1);
        //		linesParams.setMarginStart((int) getResources().getDimension(
        //				R.dimen.dp_15));
        //		linesParams.setMarginEnd((int) getResources().getDimension(
        //				R.dimen.dp_15));
        //		lines.setBackgroundColor(getResources().getColor(R.color.lines_color));
        //		linesParams.addRule(ALIGN_PARENT_BOTTOM);
        //		addView(lines, linesParams);
    }

    fun show(text: String) {
        show(text, true)
    }

    /**
     * 展示提示按钮
     *
     * @param text
     */
    fun showButton(text: String) {
        show(text, false)
    }

    private fun show(text: String, isDefultTips: Boolean?) {
        if (TextUtils.isEmpty(text))
            return

        CheackView(isDefultTips!!, text)

        startShowAnim(isDefultTips)
    }

    /**
     *
     * @param isDefult
     * textView|button
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun startShowAnim(isDefult: Boolean) {

        if (isDefult) {// 缩放动画
            if (mAnimatorSet == null) {
                mAnimatorSet = AnimatorSet()
                mAnimatorSet!!.duration = mTvStartAnimDuration.toLong()
                mAnimatorSet!!.interpolator = OvershootInterpolator()
            }
            val x = ObjectAnimator.ofFloat(mTextView, SCALE_X, 0.6f, 1f)
            val y = ObjectAnimator.ofFloat(mTextView, SCALE_Y, 0.6f, 1f)
            val z = ObjectAnimator.ofFloat(mTextView, SCALE_X, 0.6f, 1f)

            mAnimatorSet!!.play(x).with(y).with(z)
            mAnimatorSet!!.start()
            isTVShown = true
            setVisibility(true)
            //addAimatorListener(animatorSet, true);
        } else {// 平移动画
            if (mShowAction == null) {
                mShowAction = TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, -1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f)
                mShowAction!!.duration = mBtnStartAnimDuration.toLong()
            }
            startAnimation(mShowAction)
            addAimationListener(mShowAction!!, true)
        }
    }

    /**
     * 选择显示默认还是显示按钮
     *
     * @param isDefult
     * @param text
     */
    private fun CheackView(isDefult: Boolean, text: String) {

        if (isDefult) {// 显示默认提示
            setBackgroundColor(mTipsBackgroundColor)
            setContentVisibility(true)
            mTextView!!.text = text
        } else {// 显示按钮提示
            setBackgroundColor(0x00ffffff)
            setContentVisibility(false)
            //mButton.setBackgroundColor(mPropterBackgroundColor);
            btnView!!.text = text
        }
    }

    /**
     * 关闭提示按钮，如果打开了
     */
    fun hideIfNeeded() {
        if (this.visibility != View.VISIBLE)
            return
        if (isTipsBtnShown) {
            isTVShown = false
            isTipsBtnShown = false
            TranslateAnim()
        }
    }

    fun hide() {
        if (this.visibility != View.VISIBLE)
            return
        isTVShown = false
        isTipsBtnShown = false
        visibility = View.GONE
    }

    /**
     * 执行关闭的动画
     */
    fun hide(duration: Int) {
        if (this.visibility != View.VISIBLE) {//正在执行动画或则已经关闭则返回
            Log.d(TAG, "hide-Error: tips gone or is animing!")
            return
        }
        isTVShown = false
        isTipsBtnShown = false
        if (duration <= 0)
            TranslateAnim()
        else
            TranslateAnim(duration)
    }

    /**
     * 执行关闭的动画
     *
     * @param duration
     */
    private fun TranslateAnim(duration: Int = mEndAnimDuration) {

        if (mHiddenAction == null) {
            mHiddenAction = TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f)
            mHiddenAction!!.duration = duration.toLong()
        }
        startAnimation(mHiddenAction)
        addAimationListener(mHiddenAction!!, false)
    }

    /**
     *
     * @param animator
     * @param isShow
     * 展示|关闭
     */
    private fun addAimatorListener(animator: Animator, isShow: Boolean?) {
        addAnimListener(null, animator, isShow)
    }

    /**
     *
     * @param animation
     * @param isShow
     * 展示|关闭
     */
    private fun addAimationListener(animation: Animation, isShow: Boolean?) {
        addAnimListener(animation, null, isShow)
    }

    /**
     * 动画监听器
     *
     * @param animation
     * @param animator
     * @param isShow
     */
    private fun addAnimListener(animation: Animation?, animator: Animator?,
                                isShow: Boolean?) {
        animation?.setAnimationListener(object : AnimationListener {

            override fun onAnimationStart(arg0: Animation) {

            }

            override fun onAnimationRepeat(arg0: Animation) {

            }

            override fun onAnimationEnd(arg0: Animation) {
                isTipsBtnShown = true
                setVisibility(isShow!!)
            }
        })
        animator?.addListener(object : AnimatorListener {

            override fun onAnimationStart(arg0: Animator) {
                // setVisibility(isShow);
            }

            override fun onAnimationRepeat(arg0: Animator) {

            }

            override fun onAnimationEnd(arg0: Animator) {
                isTVShown = true
                setVisibility(isShow!!)
            }

            override fun onAnimationCancel(arg0: Animator) {
                isTVShown = true
                setVisibility(isShow!!)
            }
        })

    }

    private fun setContentVisibility(isDefView: Boolean) {
        if (isDefView) {
            if (btnView!!.visibility != View.GONE)
                btnView!!.visibility = View.GONE
            if (mTextView!!.visibility != View.VISIBLE)
                mTextView!!.visibility = View.VISIBLE
        } else {
            if (mTextView!!.visibility != View.GONE)
                mTextView!!.visibility = View.GONE
            if (btnView!!.visibility != View.VISIBLE)
                btnView!!.visibility = View.VISIBLE
        }
    }

    private fun setVisibility(isShow: Boolean) {
        if (isShow) {
            if (visibility != View.VISIBLE)
                visibility = View.VISIBLE
        } else {
            if (visibility != View.GONE)
                visibility = View.GONE
        }
    }
}
