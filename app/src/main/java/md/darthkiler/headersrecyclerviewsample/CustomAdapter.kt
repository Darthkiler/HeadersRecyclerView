package md.darthkiler.headersrecyclerviewsample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import darthkilersprojects.com.log.L
import kotlinx.android.synthetic.main.list_header_layout.view.*
import kotlinx.android.synthetic.main.list_item_layout.view.*
import md.darthkiler.headersrecyclerview.HeadersRecyclerViewAdapter
import md.darthkiler.headersrecyclerview.SortType
import md.darthkiler.headersrecyclerview.items.BannerItem
import md.darthkiler.headersrecyclerview.items.HeaderItem
import md.darthkiler.headersrecyclerview.items.Item

class CustomAdapter(context: Context, list:List<Item>): HeadersRecyclerViewAdapter(list, context) {

    override fun getSortType(): SortType = SortType.LAST_MODIFY

    override fun getHeadersItems(): List<HeaderItem> {
        val headerItem = object : HeaderItem() {
            override fun getLastModify(): Long = System.currentTimeMillis()
        }
        val headerItem1 = object : HeaderItem() {
            override fun getLastModify(): Long = System.currentTimeMillis() - 86400000L
        }
        return listOf(headerItem, headerItem1)
    }

    inner class CustomHolder(itemView: View) : HeadersRecyclerViewAdapter.HeadersRecyclerViewHolder(itemView) {
        override fun onBindItem(item: Item) {
            itemView.textView3.text = (item as CustomItem).getName()
        }

        override fun onBindHeader(headerItem: HeaderItem) {
            itemView.textView2.text = headerItem.getLastModify().toString()
        }

        /*override fun onBindBanner(bannerItem: BannerItem?) {

        }*/

        override fun getCounterId(): Int = R.id.textView

        override fun onOpenEditMode() {
            itemView.appCompatCheckBox.isVisible = true
        }

        override fun onCloseEditMode() {
            itemView.appCompatCheckBox.isVisible = false
        }

        override fun onItemSelectedStateChanged(selected: Boolean) {
            itemView.appCompatCheckBox.isChecked = selected
        }

    }

    fun delete(adapterPosition: Int) {
        deleteItem(adapterPosition)
        notifyItemRemoved(adapterPosition)
        L.show(getItemForPosition(adapterPosition))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        var id = -1
        when (viewType) {
            ITEM -> id = R.layout.list_item_layout
            HEADER -> id = R.layout.list_header_layout
        }

        return CustomHolder((context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(id, parent, false))
    }

}