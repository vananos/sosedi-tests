package io.github.vananos.sosedi.data

data class User(val username: String, val password: String)


val NEW_USER_WITHOUT_PROFILE = User("testuser@yandex.ru", "TestUser_1")
val USER_WITH_EXISTING_PROFILE = User("userexistingprofile@gmail.com", "TestUser_1")