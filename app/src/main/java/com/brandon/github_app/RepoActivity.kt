package com.brandon.github_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brandon.github_app.databinding.ActivityRepoBinding
import com.brandon.github_app.model.Repo
import com.brandon.github_app.network.GitHubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RepoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepoBinding
    private lateinit var repoAdapter: RepoAdapter
    private var recyclerViewState: Parcelable? = null
    var isLoading = false
    var isLastPage = false
    var currentPage = 1
    var firstVisibleItemPosition = 0

    private val retrofit = ApiClient.retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName") ?: return

        binding.tvUserName.text = userName

        repoAdapter = RepoAdapter{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl))
            startActivity(intent)
        }

        val linearLayoutManager = LinearLayoutManager(this@RepoActivity)

        binding.rvRepo.apply {
            layoutManager = linearLayoutManager
            adapter = repoAdapter
        }

        binding.rvRepo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 현재 화면에 보이는 아이템의 수
                val visibleItemCount = linearLayoutManager.childCount
                // 현재 화면에서 첫 번째로 보이는 아이템 인덱스
                firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                // RecyclerView 내에서 관리되는 총 아이템 개수
                val totalItemCount = linearLayoutManager.itemCount


                Log.d("RepoActivity", "$visibleItemCount, $firstVisibleItemPosition, $totalItemCount")

                if(!isLoading && !isLastPage){
                    // firstVisibleItem >= 0 은 아래에 대한 예외 처리
                    // RecyclerView의 아이템이 하나도 없을 때, findFirstVisibleItemPosition()이 -1을 반환할 수 있습니다.
                    if(visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0){
                        loadNextPage(userName)
                    }
                }

            }
        })

        listRepo(userName)
    }

    private fun loadNextPage(userName: String) {
        isLoading = true
        currentPage++
        listRepo(userName)
        binding.rvRepo.layoutManager?.scrollToPosition(firstVisibleItemPosition)
    }

    private fun listRepo(userName: String) {
        val gitHubService = retrofit.create(GitHubService::class.java)
        gitHubService.getReposByUserName(userName, currentPage).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.e("RepoActivity", "List Repo")
                isLastPage = response.body()?.count() != 30
                repoAdapter.submitList(repoAdapter.currentList + response.body().orEmpty())
                isLoading = false
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


}