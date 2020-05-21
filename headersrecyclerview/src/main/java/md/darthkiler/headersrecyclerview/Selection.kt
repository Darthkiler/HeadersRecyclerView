package md.darthkiler.headersrecyclerview

import android.util.SparseBooleanArray
import androidx.core.util.keyIterator
import androidx.core.util.set
import androidx.core.util.valueIterator
import md.darthkiler.headersrecyclerview.items.Item

class Selection {
    var selectionChangedCallBack: SelectionChangedCallBack? = null

    private val array = SparseBooleanArray()

    fun initList(arrayList: List<Item>) {
        array.clear()
        arrayList.iterator().forEach { array.put(it.getId(), false) }
    }

    fun isSelected(key: Int): Boolean = array[key]

    fun select(key: Int) =
        run { array[key] = true; selectionChangedCallBack?.selectionChanged() }

    fun deselect(key: Int) =
        run { array[key] = false; selectionChangedCallBack?.selectionChanged() }

    fun selectAll() = run {
        array.keyIterator()
            .forEach { array[it] = true }; selectionChangedCallBack?.selectionChanged()
    }

    fun deselectAll() = run {
        array.keyIterator()
            .forEach { array[it] = false }; selectionChangedCallBack?.selectionChanged()
    }

    fun isAllSelected(): Boolean {
        array.valueIterator().forEach {
            if (!it)
                return false
        }
        return true
    }

    fun isAllDeselected(): Boolean {
        array.valueIterator().forEach {
            if (it)
                return false
        }
        return true
    }

    fun selectedCount(): Int {
        var count = 0
        array.valueIterator().forEach { if (it) count++ }
        return count
    }

    fun removeKey(key: Int) {
        array.delete(key)
    }

    fun clear() {
        array.clear()
    }
}