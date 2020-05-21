package md.darthkiler.headersrecyclerview

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import darthkilersprojects.com.log.L
import md.darthkiler.headersrecyclerview.items.BannerItem
import md.darthkiler.headersrecyclerview.items.HeaderItem
import md.darthkiler.headersrecyclerview.items.Item
import md.darthkiler.headersrecyclerview.items.SortInterface
import java.text.FieldPosition

abstract class HeadersRecyclerViewAdapter(val context: Context): RecyclerView.Adapter<HeadersRecyclerViewAdapter.HeadersRecyclerViewHolder>() {

    private var editMode = false
    private val selection = Selection()
    private val fullList: ArrayList<SortInterface> = ArrayList()

    var onClickListener: OnClickListener? = null

    init {
        initList()
    }

    fun changeEditMode(isOpen: Boolean) {//сделать абстрактным
        editMode = isOpen
        notifyDataSetChanged()
        if (!editMode)
            selection.deselectAll()
    }

    abstract fun getList(): List<Item>
    abstract fun getSortType(): SortType
    abstract fun getHeadersItems(): List<HeaderItem>

    fun selectAll() {
        selection.selectAll()
        notifyDataSetChanged()
    }

    fun deselectAll() {
        selection.deselectAll()
        notifyDataSetChanged()
    }

    fun isAllSelected(): Boolean = selection.isAllSelected()

    fun selectedCount(): Int = selection.selectedCount()

    protected fun initList() {
        val items: List<Item> = getList()
        val sortType: SortType = getSortType()
        val headers: List<HeaderItem> = getHeadersItems()
        L.show("context", context)
        L.show("items", items)
        L.show("sortType", sortType)
        L.show("headers", headers)
        fullList.clear()
        fullList.addAll(items)

        selection.clear()
        selection.initList(items)

        fullList.addAll(headers)

        fullList.sortByDescending { it.getLastModify() } //указать сорт бай

    }

    fun selectItem(item: Item) {
        if (selection.isSelected(item.getId()))
            selection.deselect(item.getId())
        else
            selection.select(item.getId())
    }

    fun onBackPressed(): Boolean {
        if (!editMode)
            return true
        if (editMode)
            changeEditMode(false)
        return false
    }

    fun getItemForPosition(adapterPosition: Int): SortInterface = fullList[adapterPosition]

    @CallSuper
    override fun onBindViewHolder(holder: HeadersRecyclerViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM)
            holder.bindItem(getItemForPosition(position) as Item)
        if (getItemViewType(position) == HEADER)
            holder.bindHeader(getItemForPosition(position) as HeaderItem, 5)
        /*if (getItemViewType(position) == BANNER)//добавить реализацию для клика банера
            holder.onBindBanner(null)*/
    }

    override fun getItemCount(): Int = fullList.size

    override fun getItemViewType(position: Int): Int {
        if (fullList[position] is Item)
            return ITEM
        if (fullList[position] is HeaderItem)
            return HEADER
        return -1;
    }


    companion object {
        const val ITEM = 1
        const val HEADER = 2
        const val BANNER = 3
    }


    abstract inner class HeadersRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(item: Item) {
            itemView.setOnLongClickListener {
                if (!editMode) {
                    if (adapterPosition != -1) {
                        selectItem(item)
                        changeEditMode(true)
                    }
                }
                true
            }
            onBindItem(item)
            if (editMode)
                onOpenEditMode()
            else
                onCloseEditMode()

            itemView.setOnClickListener {
                if (adapterPosition != -1) {
                    if (editMode) {
                        selectItem(item)
                        notifyItemChanged(adapterPosition)
                    }
                    else
                        onClickListener?.onClick(item)
                }
            }
            onItemSelectedStateChanged(selection.isSelected(item.getId()))
        }

        fun bindHeader(headerItem: HeaderItem, itemsCount: Int) {
            setItemsForHeaderCount(itemsCount)
            onBindHeader(headerItem)
        }

        private fun setItemsForHeaderCount(count: Int) {
            itemView.findViewById<TextView>(getCounterId()).text = count.toString()
        }

        /*private fun bindBanner(bannerItem: BannerItem?) {
            onBindBanner(bannerItem)
        }*/

        //abstract fun onBindBanner(bannerItem: BannerItem?)
        @IdRes
        abstract fun getCounterId(): Int
        abstract fun onBindItem(item: Item)
        abstract fun onBindHeader(headerItem: HeaderItem)
        abstract fun onOpenEditMode()
        abstract fun onItemSelectedStateChanged(selected: Boolean)
        abstract fun onCloseEditMode()

    }
}