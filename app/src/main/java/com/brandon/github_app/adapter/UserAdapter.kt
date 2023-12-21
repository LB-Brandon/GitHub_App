package com.brandon.github_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandon.github_app.databinding.ItemUserBinding
import com.brandon.github_app.model.User

class UserAdapter(val onClick: (User) -> Unit) : ListAdapter<User, UserAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val viewBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: User) {
            viewBinding.tvUsername.text = item.userName
            viewBinding.root.setOnClickListener{
                onClick(item)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                // 두 아이템이 같은 항목인가 (예: 아이디가 같은가)
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                // 두 아이템의 내용이 완전히 같은가 (예: 모든 속성이 같은가)
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // holder 생성
        return ViewHolder(
            ItemUserBinding.inflate(
                // Activity 가 아니므로 context 가 없음. 따라서 만들어 사용
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // holder 과 item 을 연결
        holder.bind(currentList[position])
    }
}