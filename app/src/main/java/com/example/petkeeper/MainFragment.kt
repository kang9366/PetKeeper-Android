package com.example.petkeeper

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petkeeper.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        val data = this.arguments?.getString("test")
        Log.d("test", data.toString())
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerView
        val item = ArrayList<RecyclerViewData>()

        item.add(RecyclerViewData("월", "1"))
        item.add(RecyclerViewData("화", "2"))
        item.add(RecyclerViewData("수", "3"))
        item.add(RecyclerViewData("목", "4"))
        item.add(RecyclerViewData("금", "5"))
        item.add(RecyclerViewData("토", "6"))
        item.add(RecyclerViewData("일", "7"))

        val adapter = RecyclerViewAdapter(item)
//        val layoutManager = LinearLayoutManager(context)
//        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            setLayoutManager(layoutManager)
            this.adapter = adapter
        }
    }
}