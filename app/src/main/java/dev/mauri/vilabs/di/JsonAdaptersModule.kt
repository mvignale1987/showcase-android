package dev.mauri.vilabs.di

import com.squareup.moshi.JsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import dev.mauri.vilabs.network.UsersResponse

@Module
@InstallIn(SingletonComponent::class)
object JsonAdaptersModule {

    @Provides
    @ElementsIntoSet
    fun provideMoshiJsonAdapterFactory(): Set<@MoshiJsonAdapter JsonAdapter.Factory> = setOf(
        UsersResponse
    )
}
