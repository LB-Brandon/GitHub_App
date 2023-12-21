package com.brandon.github_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.github_app.adapter.UserAdapter
import com.brandon.github_app.databinding.ActivityMainBinding
import com.brandon.github_app.model.Repo
import com.brandon.github_app.model.UserDTO
import com.brandon.github_app.network.GitHubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var gitHubService: GitHubService

    val TAG: String = "MainActivity"

    private val retrofit = ApiClient.retrofit
    private var searchFor: String = ""
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter{
            val intent = Intent(this@MainActivity, RepoActivity::class.java)
            intent.putExtra("userName", it.userName)
            startActivity(intent)
        }

        binding.rvUser.apply {
            // 아래의 context 는 recycler view 의 것이다. View 도 context 를 갖는다
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
        val runnable = Runnable {
            searchUsers()
        }
        binding.etSearch.addTextChangedListener {
            // 빈번한 이벤트 발생을 방지하기 위해 디바운싱(이벤트 처리 후, 일정 시간동안 다른 이벤트 무시)
            // 비동기 처리를 해야 함. RxJava, RxKotlin, Coroutine, Handler 등
            searchFor = it.toString()
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 300)
        }

        gitHubService = retrofit.create(GitHubService::class.java)


    }

    private fun searchUsers() {
        gitHubService.getSearchUsers(searchFor).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                Log.e(TAG, response.body().toString())

                userAdapter.submitList(response.body()?.items)
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Toast.makeText(this@MainActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                // 에러 발생 시 로그 출력
                t.printStackTrace()
            }
        })
    }

}