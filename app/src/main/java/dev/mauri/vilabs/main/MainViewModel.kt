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

    private val _uiStateData = MutableStateFlow<MainUIState>(MainUIState.Initial)
    val uiStateData: StateFlow<MainUIState> = _uiStateData

    init {
        fetchUsersInfo()
    }

    fun fetchUsersInfo() {
        viewModelScope.launch {
            val resp = usersDataSource.getUsersRemoteInfo()
            if (resp.isSuccess) {
                users.addAll(0, resp.getOrNull()?.data ?: listOf())
                _uiStateData.value = MainUIState.UsersState(users.toList())
            } else {
                _uiStateData.value = MainUIState.ErrorState(
                    resp.exceptionOrNull()?.message ?: "Something bad happened"
                )
            }
        }
    }

    sealed class MainUIState {
        object Initial : MainUIState()
        class UsersState(val users: List<User>) : MainUIState()
        class ErrorState(val error: String) : MainUIState()
    }
}
