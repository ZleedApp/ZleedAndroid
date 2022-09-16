package com.zleed.app.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.zleed.app.ChannelActivity
import com.zleed.app.R

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