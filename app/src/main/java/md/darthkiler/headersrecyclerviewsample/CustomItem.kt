package md.darthkiler.headersrecyclerviewsample

import md.darthkiler.headersrecyclerview.items.Item

class CustomItem(val id1: Int, val last: Long): Item() {
    override fun getId(): Int {
        return id1
    }

    override fun getLastModify(): Long {
        return last
    }

    fun getName(): String = "Document_$id1"
}