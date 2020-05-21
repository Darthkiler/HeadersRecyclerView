package md.darthkiler.headersrecyclerviewsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import darthkilersprojects.com.log.L
import kotlinx.android.synthetic.main.activity_main.*
import md.darthkiler.headersrecyclerview.DarthkilerItemDecorator
import md.darthkiler.headersrecyclerview.HeadersRecyclerViewAdapter.Companion.HEADER
import md.darthkiler.headersrecyclerview.OnClickListener
import md.darthkiler.headersrecyclerview.items.Item
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    //повозиться с ItemDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        L.show("create adapter")
        val list = initList()
        val adapter = CustomAdapter(this, list)
        L.show("add adapter")
        recycler_view.adapter = adapter
        /*val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == HEADER)
                    2
                else
                    1
            }
        }*/
        recycler_view.addItemDecoration(DarthkilerItemDecorator(this))
        //recycler_view.layoutManager = layoutManager

        adapter.onClickListener = object : OnClickListener {
            override fun onClick(item: Item) {
                L.show((item as CustomItem).getName())
            }
        }
    }

    override fun onBackPressed() {
        val adapter = recycler_view.adapter as CustomAdapter
        if (adapter.onBackPressed())
            super.onBackPressed()
    }

    private fun initList(): List<CustomItem> {
        L.show("init list")
        val list: ArrayList<CustomItem> = arrayListOf()

        for (i in 0..111)
            list.add(CustomItem(i, when (Random().nextInt(3)) {
                0 -> (System.currentTimeMillis() - 1000L)
                1 -> (System.currentTimeMillis() - 86400000)
                2 -> (System.currentTimeMillis() - 604800000)
                else -> System.currentTimeMillis() - 1000L
            }))
        L.show("return list", list.size)
        return list
    }
}
