package dev.mauri.vilabs.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mauri.vilabs.network.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usersDataSource: UsersDataSource
) : ViewModel() {

    private val users = mutableListOf<User>()

    private val _usersData = MutableStateFlow(listOf<User>())
    val usersData: StateFlow<List<User>> = _usersData

    init {
        fetchUsersInfo()
    }

    fun fetchUsersInfo() {
        viewModelScope.launch {
            val resp = usersDataSource.getUsersRemoteInfo()
            users.addAll(0, resp?.data ?: listOf())
            _usersData.value = users.toList()
        }
    }
}
