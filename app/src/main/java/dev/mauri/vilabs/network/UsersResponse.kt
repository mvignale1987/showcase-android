package dev.mauri.vilabs.network

import android.os.Parcelable
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Type

@JsonClass(generateAdapter = true)
data class UsersResponse(

    val page: Int,
    val per_page: Int,
    val total: Int,
    val data: List<User>,
    val total_pages: Int,
) {
    companion object : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            return if (type == UsersResponse::class.java)
                UsersResponseJsonAdapter(moshi)
            else null
        }
    }
}

@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String,
) : Parcelable
