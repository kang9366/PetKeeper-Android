package com.example.petkeeper.view.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.petkeeper.R
import com.example.petkeeper.databinding.ActivityRegisterBinding
import com.example.petkeeper.util.App
import com.example.petkeeper.util.api.RetrofitBuilder
import com.example.petkeeper.util.binding.BindingActivity
import com.example.petkeeper.view.dialog.BirthPickerDialog
import com.example.petkeeper.view.dialog.PostDialog
import com.example.petkeeper.view.dialog.PostDialogData
import com.example.petkeeper.view.main.MainActivity
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class RegisterActivity : BindingActivity<ActivityRegisterBinding>(R.layout.activity_register), PostDialogData {
    private lateinit var byteArray: ByteArray
    private var age: Int = 0
    private lateinit var dialog: PostDialog
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private val viewModel: RegisterViewModel by viewModels()

    @SuppressLint("SimpleDateFormat")
    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.data = this

        initPickMedia()
        initGenderButton()
        setImage()
    }

    // 사진 선택 dialog
    fun initAddPhoto(view: View?){
        dialog = PostDialog(this)
        dialog.initDialog(pickMedia, cameraCallback)
    }

    // 이미지 설정
    private fun setImage(){
        viewModel.uri.observe(this) {
            binding.dogImage.setImageURI(it)
        }
    }

    // 갤러리 접근
    private fun initPickMedia(){
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if(it != null) {
                dialog.closeDialog()
                viewModel.setUri(it)
            }else {
                Log.d("test", "사진 선택 안됨")
            }
        }
    }

    // 카메라 접근
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }

        when(requestCode) {
            1000 -> {
                val selectedImageUri: Uri? = data?.data
                if(selectedImageUri != null){
                    binding.dogImage.setImageURI(selectedImageUri)
                    val image = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                    val stream = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    byteArray = stream.toByteArray()
                }else{
                    Toast.makeText(this, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                runCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showPermissionContextPopup(Manifest.permission.CAMERA)
            }
            else -> {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 1000)
            }
        }
    }

    private fun runCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 3000)
    }

    private fun showPermissionContextPopup(permission: String) {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 등록하기 위해서 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(permission), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    private val cameraCallback: () -> Unit = {
        initCamera()
    }

    private fun checkData(): Boolean =
                binding.editName.text.isNotEmpty() &&
                binding.editWeight.text.isNotEmpty() &&
                (binding.maleButton.isSelected || binding.femaleButton.isSelected) &&
                binding.breedSelect.isSelected &&
                binding.birthButton.isSelected

    fun initRegister(view: View){
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)

        if(checkData()){
            var gender: String? = null
            if(binding.maleButton.isSelected){
                gender = "male"
            }else if(binding.femaleButton.isSelected){
                gender = "female"
            }
            postPetData(gender!!)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postPetData(gender: String){
        RetrofitBuilder.api.postPetData("Bearer ${App.preferences.token}",
            binding.editName.text.toString(),
            binding.breedSelect.text.toString(),
            gender,
            binding.birthButton.text.toString()).enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    Log.d("testtt", response.toString())
//                    App.preferences.Pet().id = JSONObject(response.body().toString()).getInt("USER_ID").toString()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    // 품종 선택 dialog
    fun initSpinner(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setItems(R.array.category_array){
                    _, p1 ->
                binding.breedSelect.text = resources.getStringArray(R.array.category_array)[p1]
                App.preferences.Pet().breed = resources.getStringArray(R.array.category_array)[p1]
                binding.breedSelect.isSelected = true
            }
            create()
            show()
        }
    }

    // 생년월일 선택 dialog
    fun test(view: View){
        val dialog = BirthPickerDialog(this)
        dialog.initDialog()
    }

    private fun initBirth(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dateString = "${year}년 ${month+1}월 ${day}일"
            binding.birthButton.apply {
                text = dateString
                isSelected = true
                age = calculateAge(year, month+1, day)
            }
        }
        DatePickerDialog(this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).
        show()
    }

    val calculateAge: (year: Int, month: Int, day: Int) -> Int = { year, month, day ->
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH) + 1
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
        var age = currentYear - year
        if (currentMonth < month || (currentMonth == month && currentDay < day)) age--
        age
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun initGenderButton(){
        binding.maleButton.setOnClickListener {
            binding.maleButton.backgroundTintList = resources.getColorStateList(R.color.selected)
            binding.femaleButton.backgroundTintList = resources.getColorStateList(R.color.non_selected)
            binding.maleButton.isSelected = true
            binding.femaleButton.isSelected = false
        }

        binding.femaleButton.setOnClickListener {
            binding.femaleButton.backgroundTintList = resources.getColorStateList(R.color.selected)
            binding.maleButton.backgroundTintList = resources.getColorStateList(R.color.non_selected)
            binding.femaleButton.isSelected = true
            binding.maleButton.isSelected = false
        }
    }

    override fun postData(data: String) {
        binding.birthButton.text = data
        binding.birthButton.isSelected = true
    }
}