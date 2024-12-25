package com.example.tb.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Endpoint untuk login
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): Response<Map<String, Any>>

    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Response<Map<String, Any>>

    @GET("users/self")
    suspend fun getUserSelf(@Header("Authorization") token: String): Response<Map<String, Any>>

      @PUT("users")
    suspend fun updateUser(
        @Body body: Map<String, String>,  // Change from Map<String, Any?> to Map<String, String>
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

@Multipart
@PUT("users")
suspend fun updateUser1(
    @Part("username") username: RequestBody?,
    @Part("email") email: RequestBody?,
    @Part("nim") nim: RequestBody?,
    @Part image: MultipartBody.Part?,
    @Header("Authorization") token: String
): Response<Map<String, Any>>


@Multipart
@POST("products")
suspend fun createProduct(
    @Part("name") name: RequestBody,
    @Part("description") description: RequestBody,
    @Part("type") type: RequestBody,
    @Part("price") price: RequestBody,
    @Part("stock") stock: RequestBody,
    @Part image: MultipartBody.Part,
    @Header("Authorization") token: String
): Response<Map<String, Any>>


@POST("orders")
suspend fun createOrder(
    @Body body: Map<String, String>,  // Changed from Map<String, Any>
    @Header("Authorization") token: String
): Response<Map<String, Any>>


@GET("orders")
suspend fun getOrders(
    @Header("Authorization") token: String,
    @Query("class_id") class_id: String? = null
): Response<Map<String, Any>>

@GET("products")
suspend fun getProducts(
    @Query("name") name: String? = null,
    @Query("sort") sort: String? = null,
    @Header("Authorization") token: String
): Response<Map<String, Any>>

@PUT("orders/{id}")
suspend fun updateOrder(
    @Path("id") orderId: Int,
    @Body body: Map<String, Int>,
    @Header("Authorization") token: String
): Response<Map<String, Any>>


    // Endpoint untuk upload gambar
    @Multipart
    @POST("upload_image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?,
        @Header("Authorization") authorization: String
    ): Response<Map<String, Any>>

    @POST("categories")
    suspend fun createCategory(
        @Body body: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @GET("categories/{id}")
    suspend fun getCategoryById(
        @Path("id") categoryId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") categoryId: Int,
        @Body body: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("id") categoryId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @Multipart
    @POST("classes")
    suspend fun createClass(
        @Part("quota") quota: RequestBody,
        @Part("subject") subject: RequestBody,
        @Part("topic") topic: RequestBody,
        @Part("location") location: RequestBody,
        @Part("start") start: RequestBody,
        @Part("end") end: RequestBody,
        @Part("level") level: RequestBody, 
        @Part khs: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

   @GET("classes")
suspend fun getClasses(
    @Header("Authorization") token: String,
    @Query("user_id") userId: String? = null
): Response<Map<String, Any>>


    @GET("classes/{id}")
    suspend fun getClassById(
        @Path("id") classId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @Multipart
    @PUT("classes/{id}")
    suspend fun updateClass(
        @Path("id") classId: Int,
        @Part("quota") quota: RequestBody?,
        @Part("subject") subject: RequestBody?,
        @Part("topic") topic: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part khs: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

@GET("classes-user")
suspend fun getClassesByUser(
    @Header("Authorization") token: String
): Response<Map<String, Any>>


@Multipart
@POST("banksoal")
suspend fun createBankSoal(
    @Part("category_id") categoryId: RequestBody,
    @Part("level") level: RequestBody,
    @Part file: MultipartBody.Part,
    @Header("Authorization") token: String
): Response<Map<String, Any>>


    @DELETE("classes/{id}")
    suspend fun deleteClass(
        @Path("id") classId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @POST("tutors")
    suspend fun createTutor(
        @Body body: Map<String, Any>,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @GET("tutors")
    suspend fun getTutors(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @GET("tutors/{id}")
    suspend fun getTutorById(
        @Path("id") tutorId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @PUT("tutors/{id}")
    suspend fun updateTutor(
        @Path("id") tutorId: Int,
        @Body body: Map<String, Any>,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @DELETE("tutors/{id}")
    suspend fun deleteTutor(
        @Path("id") tutorId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @Multipart
    @POST("items")
    suspend fun createItem(
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @GET("items")
    suspend fun getItems(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @GET("items/{id}")
    suspend fun getItemById(
        @Path("id") itemId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @Multipart
    @PUT("items/{id}")
    suspend fun updateItem(
        @Path("id") itemId: Int,
        @Part("description") description: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

    @DELETE("items/{id}")
    suspend fun deleteItem(
        @Path("id") itemId: Int,
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>
}
