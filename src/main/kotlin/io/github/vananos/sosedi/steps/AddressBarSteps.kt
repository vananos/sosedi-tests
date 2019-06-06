package io.github.vananos.sosedi.steps

import io.github.vananos.sosedi.ConfigProperties
import io.github.vananos.sosedi.Configuration
import io.qameta.allure.Allure
import io.qameta.allure.Step

const val LOGIN_PAGE = "/"
const val REGISTRATION_PAGE = "/registration"

class AddressBarSteps : BaseSteps() {

    @Step("Open page: {address}")
    fun open(address: String) {
        val address = getFullAddress(address)
        driver.get(address)
        takeScreenShot()
    }
}

fun getFullAddress(path: String): String {
    val baseUrl = Configuration[ConfigProperties.STAGE]

    return "${baseUrl.removeSuffix("/")}/${path.removePrefix("/")}"
}