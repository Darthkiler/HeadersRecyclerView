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
import java.lang.RuntimeException
import java.text.FieldPosition

abstract class HeadersRecyclerViewAdapter(private val list: List<Item>, val context: Context): RecyclerView.Adapter<HeadersRecyclerViewAdapter.HeadersRecyclerViewHolder>() {

    //private var editMode = false
    //private val selection = Selection()
    private val fullList: ArrayList<SortInterface> = ArrayList()

    var onClickListener: OnClickListener? = null

    init {
        initList()
    }

    /*fun changeEditMode(isOpen: Boolean) {//сделать абстрактным
        editMode = isOpen
        notifyDataSetChanged()
        if (!editMode)
            selection.deselectAll()
    }*/

    abstract fun getSortType(): SortType
    abstract fun getHeadersItems(): List<HeaderItem>

    private fun calculateElementForHeaders() {
        var int = 0
        for (i in fullList.size - 1 downTo 0)
            if (fullList[i] is Item)
                int++
            else {
                if (int == 0)
                    fullList.removeAt(i)
                else
                    (fullList[i] as HeaderItem).itemsCount = int
                int = 0
            }
    }

    /*fun selectAll() {
        selection.selectAll()
        notifyDataSetChanged()
    }

    fun deselectAll() {
        selection.deselectAll()
        notifyDataSetChanged()
    }

    fun isAllSelected(): Boolean = selection.isAllSelected()

    fun selectedCount(): Int = selection.selectedCount()*/

    protected fun deleteItem(adapterPosition: Int) {
        //учитывать что позиции с рекламой будут другие
        val headerPosition = getHeaderForPosition(adapterPosition)
        val item = fullList.removeAt(adapterPosition) as Item
        notifyItemRemoved(adapterPosition)
        notifyItemChanged(headerPosition)
        deleteHeaderIfNeed(headerPosition)
        //selection.removeKey(item.getId())

        calculateElementForHeaders()
    }

    private fun getHeaderForPosition(listPosition: Int): Int {
        for (i in listPosition downTo 0)
            if (fullList[i] is HeaderItem)
                return i
        return -1
    }

    //fun isEditMode() = editMode

    private fun initList() {
        val sortType: SortType = getSortType()
        val headers: List<HeaderItem> = getHeadersItems()
        fullList.clear()
        fullList.addAll(list)

        /*selection.clear()
        selection.initList(list)*/

        fullList.addAll(headers)

        fullList.sortByDescending { it.getLastModify() } //указать сорт бай

        calculateElementForHeaders()

    }

    /*fun selectItem(item: Item) {
        if (selection.isSelected(item.getId()))
            selection.deselect(item.getId())
        else
            selection.select(item.getId())
    }*/

    private fun deleteHeaderIfNeed(adapterPosition: Int) {
        if (fullList[adapterPosition] !is HeaderItem)
            throw RuntimeException("Oops!…")
        if (adapterPosition + 1 == fullList.size || fullList[adapterPosition + 1] is HeaderItem) {
            fullList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }
    }

    fun onBackPressed(): Boolean {
        /*if (!editMode)*/
            return true
        /*if (editMode)
            changeEditMode(false)
        return false*/
    }

    fun getItemForPosition(adapterPosition: Int): SortInterface = fullList[adapterPosition]

    @CallSuper
    override fun onBindViewHolder(holder: HeadersRecyclerViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM)
            holder.bindItem(getItemForPosition(position) as Item)
        if (getItemViewType(position) == HEADER)
            holder.bindHeader(getItemForPosition(position) as HeaderItem)
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

        var viewType = ITEM

        fun bindItem(item: Item) {
            viewType = ITEM
            /*itemView.setOnLongClickListener {
                if (!editMode) {
                    if (adapterPosition != -1) {
                        selectItem(item)
                        changeEditMode(true)
                    }
                }
                true
            }*/
            onBindItem(item)
            /*if (editMode)
                onOpenEditMode()
            else
                onCloseEditMode()*/

            itemView.setOnClickListener {
                if (adapterPosition != -1) {
                    /*if (editMode) {
                        selectItem(item)
                        notifyItemChanged(adapterPosition)
                    }
                    else*/
                        onClickListener?.onClick(item)
                }
            }
            //onItemSelectedStateChanged(selection.isSelected(item.getId()))
        }

        fun bindHeader(headerItem: HeaderItem) {
            viewType = HEADER
            setItemsForHeaderCount(headerItem.itemsCount)
            onBindHeader(headerItem)
        }

        private fun setItemsForHeaderCount(count: Int) {
            itemView.findViewById<TextView>(getCounterId()).text = count.toString()
        }

        fun isItem() = viewType == ITEM

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