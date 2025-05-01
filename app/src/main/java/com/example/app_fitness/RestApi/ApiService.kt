package com.example.app_fitness.RestApi

import com.example.app_fitness.Entity.CategoryRequest
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.Entity.WorkoutLevel
import com.example.app_fitness.Response.LoginResponse
import com.example.app_fitness.Response.SignUpResponse
import com.example.app_fitness.Response.UpdateUserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @FormUrlEncoded
    @POST("signup.php")
    fun registerUser(
        @Field("fullname") fullname: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpResponse>
    @FormUrlEncoded
    @POST("login.php")
    fun signIn(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Phương thức để cập nhật thông tin người dùng, bao gồm mã người dùng, mức độ tập luyện
    @FormUrlEncoded
    @POST("update_user_info.php")
    fun submitSurvey(
        @Field("user_id") userId: Int,
        @Field("weight") weight: Float,
        @Field("height") height: Int,
        @Field("age") age: Int,
        @Field("medical_condition") medicalCondition: String,
        @Field("workout_level") workoutLevel: String,
        @Field("improvement_goal") improvementGoal: String
    ): Call<UpdateUserResponse>
    @GET("get_categories.php")
    fun getCategories(@Query("gender") gender: String): Call<List<CategoryRequest>>

    @GET("get_levels.php")
    fun getLevels(@Query("gender") gender: String): Call<List<WorkoutLevel>>
    @GET("get_excercise.php")
    fun getExercises(@Query("exercise_level_id") levelId: Int, @Query("gender") gender: String): Call<List<ExerciseRequest>>




}