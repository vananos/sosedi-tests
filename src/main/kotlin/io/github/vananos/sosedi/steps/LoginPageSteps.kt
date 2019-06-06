package io.github.vananos.sosedi.steps

import io.github.vananos.sosedi.pages.LoginPage
import io.qameta.allure.Step

class LoginPageSteps(private val loginPage: LoginPage) : BaseSteps() {

    @Step("fill login form username: {username} password: {password}")
    fun fillLoginForm(username: String, password: String) {
        loginPage.usernameInput.sendKeys(username)
        loginPage.passwordInput.sendKeys(password)
        takeScreenShot()
    }

    @Step("click on submit button")
    fun clickOnSubmitButton() {
        loginPage.submitButton.click()
        takeScreenShot()
    }

    @Step("click on not registered button")
    fun clickOnNotRegisteredButton() {
        loginPage.notRegisteredButton.click()
        takeScreenShot()
    }
}