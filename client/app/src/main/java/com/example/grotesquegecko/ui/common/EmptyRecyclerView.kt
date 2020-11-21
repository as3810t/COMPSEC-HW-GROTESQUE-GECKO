package com.example.grotesquegecko.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptyRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    var emptyView: View? = null
        set(value) {
            field = value
            checkIfEmpty()
        }

    fun checkIfEmpty() {
        val emptyView = emptyView
        val adapter = adapter

        if (emptyView != null && adapter != null) {
            val emptyViewVisible = adapter.itemCount == 0

            emptyView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            this.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = this.adapter
        oldAdapter?.unregisterAdapterDataObserver(observer)

        super.setAdapter(adapter)

        adapter?.registerAdapterDataObserver(observer)

        checkIfEmpty()
    }
}