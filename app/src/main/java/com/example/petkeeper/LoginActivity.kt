package com.example.petkeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.petkeeper.databinding.ActivityLoginBinding
import com.example.petkeeper.databinding.ActivityMainBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)

            RetrofitBuilder.api.getUserInfo("apiTest").enqueue(object : Callback<UserInfo> {
                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    val userInfo = response.body()
                    if(userInfo != null){
                        Toast.makeText(this@LoginActivity, userInfo.id, Toast.LENGTH_SHORT).show()
                        Log.d("test", userInfo.id)
                        Log.d("test", userInfo.password)

                    }
                }

                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Fail", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.naverLoginButton.setOnClickListener {
            NaverIdLoginSDK.initialize(applicationContext,
                resources.getString(R.string.naver_client_id),
                resources.getString(R.string.naver_client_secret),
                resources.getString(R.string.app_name))
            naverLogin()
        }
    }

    private fun naverLogin(){
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()

                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this@LoginActivity, oauthLoginCallback)
    }

    private fun kakaoLogin(){

    }

    private fun googleLogin(){

    }
}