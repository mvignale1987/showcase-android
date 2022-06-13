package dev.mauri.vilabs.di

import javax.inject.Qualifier

@Target(AnnotationTarget.TYPE)
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MoshiJsonAdapter
