package com.zleed.app.adapters

import com.zleed.app.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StreamListAdapter(private val streamList: ArrayList<String>) : RecyclerView.Adapter<StreamListAdapter.StreamHolder?>() {

    class StreamHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(itemView: View) {
            Log.d("RecyclerView", "CLICK!")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StreamHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view: View = layoutInflater.inflate(R.layout.video_item, parent, false)

        return StreamHolder(view)
    }

    override fun onBindViewHolder(holder: StreamHolder, position: Int) {

    }

    override fun getItemCount() = streamList.size
}
