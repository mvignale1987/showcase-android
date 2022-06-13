package dev.mauri.vilabs.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import dev.mauri.vilabs.R
import dev.mauri.vilabs.network.User

class UsersAdapter(private val imageLoader: ImageLoader) :
    androidx.recyclerview.widget.ListAdapter<User, UsersAdapter.UserViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.user_name)?.text = "${user.first_name} ${user.last_name}"

            itemView.findViewById<TextView>(R.id.user_email)?.text = user.email
            imageLoader.enqueue(
                ImageRequest.Builder(itemView.context)
                    .data(user.avatar)
                    .target(itemView.findViewById<ImageView>(R.id.user_avatar))
                    .build()
            )
            itemView.rootView.setOnClickListener {
                itemView.findNavController().navigate(
                    R.id.action_FirstFragment_to_SecondFragment, bundleOf("detailUser" to user)
                )
            }
        }
    }

    internal class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }
}
