package com.example.petkeeper.view.register

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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.petkeeper.R
import com.example.petkeeper.databinding.ActivityRegisterBinding
import com.example.petkeeper.util.App
import com.example.petkeeper.util.api.RetrofitBuilder
import com.example.petkeeper.util.binding.BindingActivity
import com.example.petkeeper.view.dialog.BirthPickerDialog
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

    @SuppressLint("SimpleDateFormat")
    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.data = this

        val intent = Intent(this@RegisterActivity, MainActivity::class.java)

        initGenderButton()

        binding.registerButton.setOnClickListener {
            initRegister(intent)
        }

        binding.birthButton.setOnClickListener {
            test()
        }

        binding.breedSelect.setOnClickListener {
            initSpinner()
        }
    }

    private fun checkData(): Boolean =
                binding.editName.text.isNotEmpty() &&
                binding.editWeight.text.isNotEmpty() &&
                (binding.maleButton.isSelected || binding.femaleButton.isSelected) &&
                binding.breedSelect.isSelected &&
                binding.birthButton.isSelected

    private fun initRegister(intent: Intent){
        if(checkData()){
            App.preferences.Pet().name = binding.editName.text.toString()
            App.preferences.Pet().weight =  binding.editWeight.text.toString().toInt()
            App.preferences.Pet().age = age
            if(binding.maleButton.isSelected){ App.preferences.Pet().gender = "male" }
            else{ App.preferences.Pet().gender = "female" }
            lateinit var gender: String
            if(binding.maleButton.isSelected){
                gender = "male"
            }else if(binding.femaleButton.isSelected){
                gender = "female"
            }

            if(binding.maleButton.isSelected){ App.preferences.Pet().gender = "male" }
            else{ App.preferences.Pet().gender = "female" }
            postPetData(gender)
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
                    App.preferences.Pet().id = JSONObject(response.body().toString()).getInt("USER_ID").toString()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
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
                    val image = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                    val stream = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    byteArray = stream.toByteArray()
                }else{
                    Toast.makeText(this, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    fun initAddPhoto(view: View?){
//        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }

    private fun initSpinner() {
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

    private fun test(){
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
        Log.d("Testt", data)
    }
}