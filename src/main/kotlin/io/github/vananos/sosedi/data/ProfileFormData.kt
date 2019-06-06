package io.github.vananos.sosedi.data

data class ProfileFormData(
    var name: String,
    var surname: String,
    var birthdate: String,
    var phone: String,
    var interests: List<String>,
    var description: String
) {
}