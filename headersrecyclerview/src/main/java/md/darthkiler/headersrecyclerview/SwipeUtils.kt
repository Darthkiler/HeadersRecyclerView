package md.darthkiler.headersrecyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import darthkilersprojects.com.log.L
import md.darthkiler.headersrecyclerview.HeadersRecyclerViewAdapter

abstract class SwipeUtils(swipeLeft: Boolean, swipeRight: Boolean, private val adapter: HeadersRecyclerViewAdapter): ItemTouchHelper.SimpleCallback(0,
    if (swipeLeft && swipeRight)
        ItemTouchHelper.START or ItemTouchHelper.END
    else if (swipeLeft)
        ItemTouchHelper.START
    else if(swipeRight)
        ItemTouchHelper.END
    else
        0) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.START) {
            onSwipeLeft(adapter, viewHolder.adapterPosition)
        }
        if (direction == ItemTouchHelper.END) {
            onSwipeRight(adapter, viewHolder.adapterPosition)
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if((viewHolder as HeadersRecyclerViewAdapter.HeadersRecyclerViewHolder).isItem()/* && !(recyclerView.adapter as HeadersRecyclerViewAdapter).isEditMode()*/)
            super.getSwipeDirs(recyclerView, viewHolder)
        else
            0
    }

    abstract fun onSwipeLeft(adapter: HeadersRecyclerViewAdapter, adapterPosition: Int)
    abstract fun onSwipeRight(adapter: HeadersRecyclerViewAdapter, adapterPosition: Int)

}