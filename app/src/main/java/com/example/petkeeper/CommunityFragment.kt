package com.example.petkeeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petkeeper.databinding.FragmentCommunityBinding
import com.example.petkeeper.databinding.FragmentMainBinding

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerView
        val item = ArrayList<Data>()
        item.add(Data("test"))
        item.add(Data("test"))
        item.add(Data("test"))
        item.add(Data("test"))
        item.add(Data("test"))
        item.add(Data("test"))
        item.add(Data("test"))

        val adapter = RecyclerViewAdapter(item)
        recyclerView.adapter = adapter
    }
}