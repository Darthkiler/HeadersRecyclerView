package md.darthkiler.headersrecyclerview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class DarthkilerItemDecorator(val context: Context): RecyclerView.ItemDecoration() {

    var mDivider: Drawable? = null

    private val mBounds = Rect()

    init {
        val a = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if(parent.layoutManager == null || mDivider == null)
            return
        drawItem(c, parent)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        mDivider?.let {
            outRect.set(0,0,0, it.intrinsicHeight)
        } ?: run {
            outRect.set(0, 0, 0, 0)
        }
    }

    private fun drawItem(canvas: Canvas, recyclerView: RecyclerView) {
        canvas.save()

        val left: Int
        val right: Int

        if (recyclerView.clipToPadding) {
            left = recyclerView.paddingLeft + dpToPx(8)
            right = recyclerView.width - recyclerView.paddingRight - dpToPx(8)
            canvas.clipRect(
                left, recyclerView.paddingTop, right,
                recyclerView.height - recyclerView.paddingBottom
            )
        } else {
            left = 0 + dpToPx(8)
            right = recyclerView.width - dpToPx(8)
        }

        val childCount: Int = recyclerView.childCount
        for (i in 0 until childCount) {
            val child: View = recyclerView.getChildAt(i)
            if (child.tag == null || child.tag != "no_decorator")
            {
                recyclerView.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom = mBounds.bottom + child.translationY.roundToInt()
                val top = bottom - mDivider!!.intrinsicHeight
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun dpToPx(dp: Int): Int {
        return ((dp * Resources.getSystem().displayMetrics.density).roundToInt())
    }
}