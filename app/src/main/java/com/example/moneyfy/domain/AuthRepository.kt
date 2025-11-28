package com.example.moneyfy.domain

import com.example.moneyfy.data.model.User

class AuthRepository {

    private val fakeUser = User(
        username = "admin",
        email = "admin@gmail.com",
        password = "123456"
    )

    fun login(email: String, password: String): Boolean {
        return email == fakeUser.email && password == fakeUser.password
    }

    fun register(username: String, email: String, password: String): Boolean {
        // Trong thực tế bạn sẽ lưu vào database
        return true
    }
}
