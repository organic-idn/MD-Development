package com.example.organic.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.organic.ui.userpreference.UserPreferences
import kotlinx.coroutines.launch

class HomeViewModel (private val preferences: UserPreferences):ViewModel(){
    fun getRefreshToken(): LiveData<String> = preferences.getRefreshToken().asLiveData()

    fun logout() {
        viewModelScope.launch{
            preferences.logout()
        }
    }
}

