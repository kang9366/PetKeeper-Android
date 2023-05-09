package com.example.petkeeper

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petkeeper.databinding.FragmentMainBinding
import java.io.ByteArrayOutputStream

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var name: String
    private lateinit var image: Bitmap
    private var isFabOpen = false
    private lateinit var mainActivity: MainActivity

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
        binding.dogImage.setImageBitmap(image)

        binding.fabMain.apply {
            bringToFront()
            setOnClickListener {
                toggleFab()
            }
        }

        binding.dogImage.apply {
            setOnClickListener {
                initAddPhoto()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        name = arguments?.getString("name").toString()
        val bitmap = arguments?.getByteArray("image")!!
        image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)

        mainActivity = context as MainActivity
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

    private fun initAddPhoto(){
        //갤러리 접근 권한 설정
        when {
            ContextCompat.checkSelfPermission(
                mainActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                //권한이 부여되었을 때 갤러리에서 사진 선택
                navigatePhoto()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }

        when(requestCode) {
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                if(selectedImageUri != null){
                    binding.dogImage.setImageURI(selectedImageUri)
                }else{
                    Toast.makeText(mainActivity, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(mainActivity, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(mainActivity)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 등록하기 위해서 권한이 필요합니다.")
            .setPositiveButton("동의하기", {dialog, which ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            })
            .setNegativeButton("취소하기", {_, _-> })
            .create()
            .show()
    }

    private fun navigatePhoto(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }
}