package com.zleed.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.zleed.app.ChannelActivity
import com.zleed.app.R
import com.zleed.app.adapters.StreamListAdapter


class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = inflater.inflate(R.layout.fragment_home, container, false)

        val openButton: MaterialButton = layoutInflater.findViewById(R.id.button)

        val recyclerView: RecyclerView = layoutInflater.findViewById(R.id.recyclerView)

        val videoList: ArrayList<String> = ArrayList<String>();

        videoList.add("1a")
        videoList.add("2a")
        videoList.add("3a")
        videoList.add("4a")

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val listAdapter = StreamListAdapter(videoList)
        recyclerView.adapter = listAdapter

        openButton.setOnClickListener { _ ->
            val i1 = Intent()

            i1.setClass(requireContext(), ChannelActivity::class.java)
            startActivity(i1)
        }

        return layoutInflater
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}