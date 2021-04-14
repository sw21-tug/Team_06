package com.team06.focuswork.ui.overview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team06.focuswork.R
import com.team06.focuswork.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment() {

    private lateinit var overviewModel: OverviewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        overviewModel = ViewModelProvider(this).get(OverviewModel::class.java)
        binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.adapter = TaskAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}