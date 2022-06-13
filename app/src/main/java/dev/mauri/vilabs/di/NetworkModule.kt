package dev.mauri.vilabs.di

import android.app.Application
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mauri.vilabs.network.ReqresApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun moshi(
        jsonAdapterFactory: Set<@JvmSuppressWildcards @MoshiJsonAdapter JsonAdapter.Factory>,
    ): Moshi = Moshi.Builder()
        .also { builder -> jsonAdapterFactory.forEach { builder.add(it) } }
        .build()

    @Singleton
    @Provides
    @ExperimentalCoilApi
    fun imageLoader(
        application: Application,
        okHttpClient: OkHttpClient,
    ): ImageLoader {
        return ImageLoader.Builder(application.applicationContext)
            .crossfade(true)
            .okHttpClient(okHttpClient)
            .diskCache {
                DiskCache
                    .Builder()
                    .directory(application.applicationContext.cacheDir)
                    .build()
            }
            .memoryCache {
                MemoryCache
                    .Builder(application.applicationContext)
                    .maxSizePercent(0.6)
                    .build()
            }
            .build()
    }

    @Singleton
    @Provides
    fun retrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://reqres.in")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Singleton
    @Provides
    fun channelApi(retrofit: Retrofit): ReqresApi = retrofit.create(ReqresApi::class.java)
}
