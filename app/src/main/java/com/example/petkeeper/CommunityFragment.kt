package com.example.petkeeper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.example.petkeeper.databinding.FragmentCommunityBinding

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

        for(i in 0..8){
            item.add(Data("test"))
        }

        val adapter = RecyclerViewAdapter(item)
        recyclerView.adapter = adapter

        binding.postButton.setImageResource(R.drawable.post)
        initPostDialog()
    }

    private fun initPostDialog(){
        binding.postButton.setOnClickListener{
            val dialogView = layoutInflater.inflate(R.layout.post_dialog, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            dialog.setOnShowListener {
                dialog.window?.apply {
                    setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                    setGravity(Gravity.BOTTOM)
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.show()
        }
    }
}