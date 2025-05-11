package com.example.app_fitness.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_fitness.RestApi.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class FoodEntryViewModel : ViewModel() {
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun saveFoodEntry(userId: Int, foodName: String, quantity: Int) {
        if (userId != -1 && foodName.isNotEmpty()) {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val currentTimestamp = Date().time / 1000 // Lấy timestamp giây
                val call = RetrofitClient.instance.saveCalorieEntry(userId, foodName, quantity)
                try {
                    val response = call.execute()
                    if (response.isSuccessful) {
                        message.postValue("Đã lưu: $foodName")
                    } else {
                        message.postValue("Lỗi khi lưu: $foodName")
                    }
                } catch (e: Exception) {
                    message.postValue("Lỗi mạng khi lưu: ${e.message}")
                } finally {
                    isLoading.postValue(false)
                }
            }
        } else if (userId == -1) {
            message.value = "Chưa có thông tin người dùng để lưu."
        } else {
            message.value = "Tên món ăn không được để trống."
        }
    }
}