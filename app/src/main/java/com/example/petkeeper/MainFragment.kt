package com.example.petkeeper

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petkeeper.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var name: String
    private var isFabOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameText.text = name

        binding.fabMain.apply {
            bringToFront()
            setOnClickListener {
                toggleFab()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        name = arguments?.getString("name").toString()
    }

    private fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabSub1, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabSub2, "translationY", 0f).apply { start() }
            binding.fabMain.setImageResource(R.drawable.ic_up)
        } else {
            ObjectAnimator.ofFloat(binding.fabSub1, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabSub2, "translationY", -400f).apply { start() }
            binding.fabMain.setImageResource(R.drawable.ic_down)
        }
        isFabOpen = !isFabOpen
    }
}