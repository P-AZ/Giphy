package com.example.giphy.remote

//this class will be a wrapper to the response from the server
//using sealed class to make sure only the classes here can inherit from Resources
sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null
) {
    class Succeess<T>(data: T) : Resource<T>(data)
    // sometimes its error but data is not null
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message)
}