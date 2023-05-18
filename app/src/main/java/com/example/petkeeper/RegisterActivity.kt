package com.example.petkeeper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.petkeeper.databinding.ActivityRegisterBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var byteArray: ByteArray

    @SuppressLint("SimpleDateFormat")
    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this@RegisterActivity, MainActivity::class.java)

        initGenderButton()
        binding.addButton.setOnClickListener {
            initAddPhoto()
        }

        binding.registerButton.setOnClickListener {
            if(checkData()) {
                initDogData(intent)
                startActivity(intent)
            }else{
                Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.birthButton.setOnClickListener {
            initBirth()
        }

        binding.breedSelect.setOnClickListener {
            initSpinner()
        }
    }

    private fun checkData(): Boolean{
        return binding.editName.text.isNotEmpty() &&
                binding.editWeight.text.isNotEmpty() &&
                (binding.maleButton.isSelected || binding.femaleButton.isSelected) &&
                binding.breedSelect.isSelected &&
                binding.birthButton.isSelected
    }

    private fun initDogData(intent: Intent){
        try {
            intent.putExtra("image", byteArray)
        } catch (e: UninitializedPropertyAccessException) {
            val drawable: Drawable = ContextCompat.getDrawable(applicationContext, R.drawable.logo)!!
            val bitmap = (drawable as BitmapDrawable).bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            byteArray = byteArrayOutputStream.toByteArray()
            intent.putExtra("image", byteArray)
        }
        intent.putExtra("weight", binding.editWeight.text.toString())
        intent.putExtra("name", binding.editName.text.toString())
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
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

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 등록하기 위해서 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    navigatePhoto()
                }else {
                    Toast.makeText(this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> { }
        }
    }

    private fun initAddPhoto(){
        //갤러리 접근 권한 설정
        when {
            ContextCompat.checkSelfPermission(
                this,
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



    private fun initSpinner() {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setItems(R.array.category_array){
                    _, p1 ->
                binding.breedSelect.text = resources.getStringArray(R.array.category_array)[p1]
                binding.breedSelect.isSelected = true
            }
            create()
            show()
        }
    }

    private fun initBirth(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
            binding.birthButton.apply {
                text = dateString
                isSelected = true
            }
        }
        DatePickerDialog(this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).
        show()
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
}