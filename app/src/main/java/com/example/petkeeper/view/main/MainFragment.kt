package com.example.petkeeper.view.main

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.petkeeper.R
import com.example.petkeeper.util.api.RetrofitBuilder
import com.example.petkeeper.databinding.FragmentMainBinding
import com.example.petkeeper.util.App
import com.example.petkeeper.util.adapter.DateAdapter
import com.example.petkeeper.util.adapter.DateItem
import com.example.petkeeper.util.adapter.RecyclerViewAdapter
import com.example.petkeeper.util.binding.BindingFragment
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main, true) {
    private lateinit var name: String
    private lateinit var image: Bitmap
    private var isFabOpen = false
    private lateinit var mainActivity: MainActivity
    val calendar: Calendar = Calendar.getInstance()
    private val item = ArrayList<DateItem>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.nameText?.text = App.preferences.Pet().name
        binding?.weightText?.text = "${App.preferences.Pet().weight}kg"
        binding?.ageText?.text = "${App.preferences.Pet().age}살"

        if(App.preferences.Pet().gender == "male"){
            binding?.genderImage?.setImageResource(R.drawable.male_icon)
        }else{
            binding?.genderImage?.setImageResource(R.drawable.female_icon)
        }

        binding?.fabMain?.apply {
            bringToFront()
            setOnClickListener {
                toggleFab()
            }
        }

        binding?.dogImage?.apply {
            setOnClickListener {
                initAddPhoto()
            }
        }

        binding?.fabSub1?.setOnClickListener {
            initCamera()
        }

        binding?.yearMonthText?.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH)+1}월"
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        for(i in 1 ..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            item.add(DateItem(i, getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val adapter = DateAdapter(item)
        binding?.dateRecyclerView?.adapter = adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        name = arguments?.getString("name").toString()
//        val bitmap = arguments?.getByteArray("image")!!
//        image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)

        mainActivity = context as MainActivity
    }

    private fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding?.fabSub1, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabSub2, "translationY", 0f).apply { start() }
            binding?.fabMain?.setImageResource(R.drawable.ic_examination)
        } else {
            ObjectAnimator.ofFloat(binding?.fabSub1, "translationY", -250f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabSub2, "translationY", -500f).apply { start() }
            binding?.fabMain?.setImageResource(R.drawable.ic_down)
        }
        isFabOpen = !isFabOpen
    }

    private fun initCamera() {
        when {
            ContextCompat.checkSelfPermission(
                mainActivity,
                CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                runCamera()
            }
            shouldShowRequestPermissionRationale(CAMERA) -> {
                showPermissionContextPopup(CAMERA)
            }
            else -> {
                requestPermissions(arrayOf(CAMERA), 1000)
            }
        }
    }

    private fun initAddPhoto(){
        when {
            ContextCompat.checkSelfPermission(
                mainActivity,
                READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                navigatePhoto()
            }
            shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup(READ_EXTERNAL_STORAGE)
            }
            else -> {
                requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 1000)
            }
        }
    }

    fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }
        val selectedImageUri: Uri? = data?.data

        when(requestCode) {
            2000 -> {
                if(selectedImageUri != null){
                    binding?.dogImage?.setImageURI(selectedImageUri)
                }else{
                    Toast.makeText(mainActivity, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
                }
            }

            3000 -> {
                val extras = data?.extras
                val bitmap = extras?.get("data") as Bitmap?
                val rotatedBitmap = rotateBitmap(bitmap!!, 90f)
                val file: File = convertBitmapToFile(rotatedBitmap)

                val survey = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val multipart = MultipartBody.Part.createFormData("image", "download.jpg", survey)
                postImage(multipart)
            }

            else -> {
                Toast.makeText(mainActivity, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup(permission: String) {
        AlertDialog.Builder(mainActivity)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 등록하기 위해서 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(permission), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    private fun navigatePhoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    private fun runCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 3000)
    }

    private fun getAbsolutePathFromUri(uri: Uri, context: Context): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var path: String? = null
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = cursor.getString(columnIndex)
            }
        }
        return path
    }

    private fun convertBitmapToFile(bitmap: Bitmap): File{
        val file = File(mainActivity.filesDir, "picture")
        val output = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        return file
    }

    private fun postImage(body: MultipartBody.Part){
        RetrofitBuilder.api.postEyeImage(body).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("post eye image", response.toString())
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("post eye image", t.message.toString())
            }
        })
    }
    private fun getDayOfWeek(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "일"
            Calendar.MONDAY -> "월"
            Calendar.TUESDAY -> "화"
            Calendar.WEDNESDAY -> "수"
            Calendar.THURSDAY -> "목"
            Calendar.FRIDAY -> "금"
            Calendar.SATURDAY -> "토"
            else -> ""
        }
    }
}