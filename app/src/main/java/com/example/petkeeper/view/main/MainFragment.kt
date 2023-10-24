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
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.petkeeper.R
import com.example.petkeeper.util.api.RetrofitBuilder
import com.example.petkeeper.databinding.FragmentMainBinding
import com.example.petkeeper.model.UserResponse
import com.example.petkeeper.util.App.Companion.preferences
import com.example.petkeeper.util.adapter.DateAdapter
import com.example.petkeeper.util.adapter.DateItem
import com.example.petkeeper.util.adapter.OnItemClickListener
import com.example.petkeeper.util.binding.BindingFragment
import com.example.petkeeper.view.dialog.DetailDialog
import com.example.petkeeper.view.dialog.TestViewModel
import com.example.petkeeper.view.dialog.VaccinationDialog
import com.example.petkeeper.view.dialog.WeightDialog
import com.google.android.gms.common.wrappers.Wrappers
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main, true) {
    private val viewModel: TestViewModel by activityViewModels()

    private var isFabOpen = false
    private lateinit var context: MainActivity
    private var item = ArrayList<DateItem>()
    private val calendar = Calendar.getInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context as MainActivity
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInformation()
        initRecyclerView()
        getUserData()


        binding?.fabMain?.apply {
            bringToFront()
            setOnClickListener {
                toggleFab()
            }
        }

        binding?.fabSub1?.setOnClickListener {
            requestCameraPermission()
        }

        binding?.vaccinationDateText?.setOnClickListener {
            val dialog = VaccinationDialog(context)
            dialog.initDialog()
        }

        binding?.weightText?.setOnClickListener {
            val dialog = WeightDialog(context)
            dialog.initDialog()
        }
    }

    private fun getUserData(){
        RetrofitBuilder.api.getUserInfo(userId=preferences.userId.toString()).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val responseData = response.body()!!.p_pets[0]
                binding?.nameText?.text = responseData.PET_NAME
                binding?.ageText?.text = responseData.PET_BIRTHDATE
                binding?.breedText?.text = responseData.PET_KIND
                if(responseData.PET_GENDER == "male"){
                    binding?.genderImage?.setImageResource(R.drawable.male_icon)
                }else{
                    binding?.genderImage?.setImageResource(R.drawable.female_icon)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }

        })
    }

    fun saveUriToFile(context: Context, uri: Uri): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp.jpg"

        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val file = File(storageDir, fileName)
                val fos = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    fos.write(buffer, 0, read)
                }
                fos.close()
                inputStream.close()
                return file
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    @SuppressLint("SetTextI18n")
    private fun initInformation(){
        binding?.weightText?.text = "${preferences.Pet().weight}kg"
        binding?.yearMonthText?.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH)+1}월"
    }

    private fun initDateItem(){
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        for(i in 1 ..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            item.add(DateItem(i, getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    private fun initRecyclerView() {
        initDateItem()
        val adapter = DateAdapter(item)
        adapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(v: View, data: DateItem, pos: Int) {
                val dialog = DetailDialog(context, viewModel)
                dialog.initDialog(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, data)
            }
        })
        binding?.dateRecyclerView?.adapter = adapter
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

    private fun requestCameraPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(requireContext(), CAMERA) -> {
                initCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(CAMERA)
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun initCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            takePictureLauncher.launch(takePictureIntent)
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp.jpg"

        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return try {
            val file = File(storageDir, fileName)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            val file = saveBitmapToFile(context, bitmap)
            val fileRequestBody = file!!.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("image", file.name, fileRequestBody)
            postImage(filePart)
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            initCamera()
        } else {
            // 권한을 거부한 경우
        }
    }

    private fun bitmapToMultipart(bitmap: Bitmap, fileName: String): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData("image ", fileName, requestBody)
    }



    private fun postImage(body: MultipartBody.Part){
        RetrofitBuilder.api.postEyeImage(image = body).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("post eye image", response.body().toString())
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("post eye image error", t.message.toString())
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