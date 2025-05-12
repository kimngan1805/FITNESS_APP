package com.example.app_fitness.RestApi

import com.example.app_fitness.Entity.AddedExercise
import com.example.app_fitness.Entity.CategoryRequest
import com.example.app_fitness.Entity.Comment
import com.example.app_fitness.Entity.DailyFoodItem
import com.example.app_fitness.Entity.ExerciseRequest
import com.example.app_fitness.Entity.FeedItem
import com.example.app_fitness.Entity.FoodItem
import com.example.app_fitness.Entity.MarkCompletedRequest
import com.example.app_fitness.Entity.UserData
import com.example.app_fitness.Entity.UserInfoRequest
import com.example.app_fitness.Entity.WorkoutLevel
import com.example.app_fitness.Response.CaloriesBurnedResponse
import com.example.app_fitness.Response.CommentCountResponse
import com.example.app_fitness.Response.HasExercisesResponse
import com.example.app_fitness.Response.LoginResponse
import com.example.app_fitness.Response.SignUpResponse
import com.example.app_fitness.Response.UpdateUserResponse
import com.example.app_fitness.Response.UserDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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
        @Field("improvement_goal") improvementGoal: String,
        @Field("gender") gender:String
    ): Call<UpdateUserResponse>
    @GET("get_categories.php")
    fun getCategories(@Query("gender") gender: String): Call<List<CategoryRequest>>

    @GET("get_levels.php")
    fun getLevels(@Query("gender") gender: String): Call<List<WorkoutLevel>>
    @GET("get_excercise.php")
    fun getExercises(@Query("exercise_level_id") levelId: Int, @Query("gender") gender: String): Call<List<ExerciseRequest>>
    @GET("get_user_data.php")
    fun getUserData(
        @Query("user_id") userId: Int
    ): Call<UserDataResponse>
    @FormUrlEncoded
    @POST("measurement_api.php") // thay bằng tên file PHP xử lý
    fun saveMeasurement(
        @Field("user_id") userId: Int,
        @Field("date") date: String,
        @Field("result") result: String,
        @Field("weight") weight: Float,
        @Field("height") height: Int,
        @Field("body_fat") bodyFat: Int,
        @Field("neck") neck: Float,
        @Field("shoulder") shoulder: Float,
        @Field("chest") chest: Float,
        @Field("waist") waist: Float
    ): Call<UserDataResponse>


    @FormUrlEncoded
    @POST("add_excercise_user.php") // Tên file PHP trên server để thêm bài tập
    fun addUserExercise(
        @Field("exercise_id") exerciseId: Int,
        @Field("user_id") userId: Int
    ): Call<Void> // Hoặc một Response cụ thể nếu server trả về dữ liệu sau khi thêm

    @GET("get_exercise_by_id.php?action=get_exercise_by_id")
    fun getExerciseById(@Query("exercise_id") exerciseId: Int): Call<ExerciseRequest>

    @GET("get_exercise_by_id.php?action=get_user_exercises")
    fun getUserExercises(@Query("user_id") userId: Int): Call<List<AddedExercise>>
    @GET("check_user_exercises.php")
    fun checkUserHasExercises(@Query("user_id") userId: Int): Call<HasExercisesResponse>

    @GET("get_exercises_training.php")
    fun getExercises(
        @Query("level_id") levelId: Int? = null,
        @Query("gender") gender: String? = null,
        @Query("user_id") userId: Int? = null // Thêm user_id
    ): Call<List<ExerciseRequest>>
    @GET("get_feeds.php") // Đảm bảo đường dẫn này đúng với API của bạn
    fun getFeeds(): Call<List<FeedItem>>
    @GET("get_comments.php")
    fun getComments(@Query("post_id") postId: Int): Call<List<Comment>>
    @FormUrlEncoded
    @POST("add_comment.php") // Endpoint để thêm bình luận
    fun addComment(
        @Field("post_id") postId: Int,
        @Field("user_id") userId: Int,
        @Field("comment_text") commentText: String,
        @Field("created_at") createdAt: String
    ): Call<Void>// Hoặc Response bạn mong đợi

    @GET("get_comment_count.php/{post_id}")
    fun getCommentCount(@Query("post_id") postId: Int): Call<CommentCountResponse>

    @FormUrlEncoded
    @POST("mark_exercise_completed.php")
    fun markExerciseCompleted(
        @Field("user_id") userId: Int,
        @Field("exercise_id") exerciseId: Int
    ): Call<Void>

    @GET("get_completed_exercises.php") // Thêm endpoint mới
    fun getCompletedExercises(@Query("user_id") userId: Int): Call<List<Int>>


    @GET("get_next_training_exercises.php")
    fun getNextTrainingExercise(@Query("user_id") userId: Int): Call<ExerciseRequest?>

    @FormUrlEncoded
    @POST("save_calorie_entry.php") // Endpoint PHP để lưu một mục calo
    fun saveCalorieEntry(
        @Field("user_id") user_id: Int,
        @Field("food_name") food_name: String,
        @Field("quantity") quantity: Int,
    ): Call<Void>

    @GET("get_daily_food_list.php")
    fun getDailyFoodList(
        @Query("user_id") userId: Int,
        @Query("date") date: String // Định dạng YYYY-MM-DD
    ): Call<List<DailyFoodItem>>

    @GET("get_food_details.php") // Endpoint mới
    fun getFoodDetails(@Query("food_name") foodName: String): Call<FoodItem>

    @GET("get_user_analysis.php") // Endpoint mới
    fun getUserAnalysis(@Query("user_id") userId: Int): Call<UserInfoRequest>
    @GET("get_calories_excercise.php")  // Đặt tên file PHP của bạn
    fun getCaloriesBurned(@Query("user_id") userId: Int): Call<CaloriesBurnedResponse>
}