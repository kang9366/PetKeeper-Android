package com.example.petkeeper.view.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.petkeeper.R
import com.example.petkeeper.databinding.ActivitySignUpBinding
import com.example.petkeeper.model.UserInfo
import com.example.petkeeper.util.api.RetrofitBuilder
import com.example.petkeeper.util.binding.BindingActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpActivity : BindingActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.data = this
    }

    fun initSignUp(view: View){
        if(binding.editName.text.isNotEmpty() &&
            binding.editEmail.text.isNotEmpty() &&
            binding.editPhone.text.isNotEmpty() &&
            (binding.editPassword.text.toString() == binding.editCheckPassword.text.toString())){
            postData(binding.editEmail.text.toString(), binding.editCheckPassword.text.toString(), binding.editPhone.text.toString())
            finish()
        }else{
            Toast.makeText(this, "모든 정보를 입력해주세요!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postData(email: String, password: String, phone: String){
        RetrofitBuilder.api.postSignUpData(userInfo = UserInfo(email, password, phone)).enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    if(response.code()==200){
                        Toast.makeText(this@SignUpActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@SignUpActivity, "이메일이 중복되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                Log.d("Sign Up", t.message.toString())
            }
        })
    }
}