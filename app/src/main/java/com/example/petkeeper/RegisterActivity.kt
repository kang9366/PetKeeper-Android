package com.example.petkeeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.petkeeper.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSpinner()
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.breedSelect.adapter = adapter
        }

        binding.breedSelect.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p0 != null) {
                        Toast.makeText(applicationContext,p0.getItemAtPosition(p2).toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    return
                }
            }
    }

}