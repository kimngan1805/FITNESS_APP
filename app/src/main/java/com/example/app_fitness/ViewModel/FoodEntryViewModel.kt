package com.example.app_fitness.ViewModel
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_fitness.Entity.DailyFoodItem
import com.example.app_fitness.Entity.UserInfoRequest
import com.example.app_fitness.RestApi.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import android.content.Intent
import androidx.lifecycle.AndroidViewModel

// Thay ViewModel bằng AndroidViewModel để có quyền truy cập vào Context
class FoodEntryViewModel(application: Application) : AndroidViewModel(application) {
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val dailyCalories = MutableLiveData<Double>()
    val dailyFoodList = MutableLiveData<List<DailyFoodItem>>()
    val predictedCalories = MutableLiveData<Double>()
    private var userInfoRequest: UserInfoRequest? = null
    // Lấy context từ application
    private val context: Context = application.applicationContext

    fun saveFoodEntry(userId: Int, foodName: String, quantity: Int) {
        if (userId != -1 && foodName.isNotEmpty()) {
            isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val currentTimestamp = Date().time / 1000
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

    fun fetchDailyFoodList(userId: Int, date: String) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val call = RetrofitClient.instance.getDailyFoodList(userId, date)
                val response = call.execute()
                if (response.isSuccessful) {
                    val foodList = response.body() ?: emptyList()
                    dailyFoodList.postValue(foodList) // Post danh sách món ăn lên LiveData
                    calculateDailyCalories(foodList) // Tính calo ngay sau khi lấy danh sách
                } else {
                    withContext(Dispatchers.Main) {
                        message.value = "Lỗi khi tải thực đơn hôm nay"
                    }
                    dailyFoodList.postValue(emptyList())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    message.value = "Lỗi mạng khi tải thực đơn hôm nay: ${e.message}"
                }
            } finally {
                isLoading.postValue(false)
            }
        }
    }
    private fun calculateDailyCalories(foodList: List<DailyFoodItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            var totalCalories = 0.0
            for (dailyFoodItem in foodList) {
                try {
                    val foodDetailsCall = RetrofitClient.instance.getFoodDetails(dailyFoodItem.foodName)
                    val foodDetailsResponse = foodDetailsCall.execute()
                    if (foodDetailsResponse.isSuccessful) {
                        val foodItem = foodDetailsResponse.body()
                        if (foodItem != null) {
                            totalCalories += (dailyFoodItem.quantity * foodItem.foodCalories) / foodItem.foodGram
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            message.value =
                                "Không tìm thấy thông tin calo cho món: ${dailyFoodItem.foodName}"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        message.value =
                            "Lỗi khi lấy thông tin calo cho món ${dailyFoodItem.foodName}: ${e.message}"
                    }
                }
            }
            withContext(Dispatchers.Main) {
                dailyCalories.value = totalCalories
            }
        }
    }

    fun fetchUserData() { // Không cần userId làm tham số nữa
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Lấy userId từ SharedPreferences
                val sharedPref = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val userId = sharedPref.getInt("user_id", -1)
                Log.d("FoodEntryViewModel", "User ID from SharedPref: $userId") // Kiểm tra giá trị

                // Kiểm tra xem có lấy được userId không
                if (userId != -1) {
                    val call = RetrofitClient.instance.getUserAnalysis(userId)
                    val response = call.execute()
                    if (response.isSuccessful) {
                        userInfoRequest = response.body()
                        if (userInfoRequest != null) {
                            Log.d("FoodEntryViewModel", "User data: $userInfoRequest")
                            val allowedCalories = calculatePredictedCalories(
                                userInfoRequest!!.weight,
                                userInfoRequest!!.height,
                                userInfoRequest!!.age,
                                userInfoRequest!!.workout_level ?: "",
                                userInfoRequest!!.improvement_goal ?: "",
                                userInfoRequest!!.gender
                            )
                            withContext(Dispatchers.Main) {
                                predictedCalories.value = allowedCalories
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                message.value = "Failed to retrieve user data"
                                predictedCalories.value = 0.0
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            message.value =
                                "Error fetching user data: ${response.code()} - ${response.message()}"
                            predictedCalories.value = 0.0
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        message.value = "User ID not found. Please log in again."
                        predictedCalories.value = 0.0
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    message.value = "Failed to connect to server: ${e.message}"
                    predictedCalories.value = 0.0
                }
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    fun calculatePredictedCalories(
        weight: Float,
        height: Float,
        age: Int,
        workoutLevel: String,
        improvementGoal: String,
        gender: String
    ): Double {
        Log.d("FoodEntryViewModel", "weight: $weight, height: $height, age: $age, workoutLevel: $workoutLevel, improvementGoal: $improvementGoal, gender: $gender")
        // Tính BMR theo công thức Mifflin-St Jeor
        val bmr = (10 * weight) + (6.25 * height) - (5 * age) + if (gender == "Nữ") 5 else -161
        Log.d("FoodEntryViewModel", "BMR: $bmr")

        // Hệ số hoạt động thể chất
        val activityFactor = when (workoutLevel) {
            "Ít vận động" -> 1.2
            "Vận động nhẹ" -> 1.375
            "Vận động vừa" -> 1.55
            "Vận động nhiều" -> 1.725
            "Vận động rất nhiều" -> 1.9
            else -> 1.2
        }
        Log.d("FoodEntryViewModel", "Activity Factor: $activityFactor")

        // Tính TDEE
        var tdee = bmr * activityFactor
        Log.d("FoodEntryViewModel", "TDEE before goal adjustment: $tdee")

        // Điều chỉnh TDEE dựa trên mục tiêu
        when (improvementGoal) {
            "Giảm cân" -> tdee *= 0.85
            "Tăng cân" -> tdee *= 1.15
            "Giữ dáng" -> tdee *= 1.0
            "mo bung" -> tdee *= 0.9
            else -> tdee
        }
        Log.d("FoodEntryViewModel", "Final TDEE: $tdee")
        return tdee
    }
}