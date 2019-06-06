package io.github.vananos.sosedi.pages

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy

class LoginPage {

    @FindBy(name = "username")
    lateinit var usernameInput: WebElement

    @FindBy(name = "password")
    lateinit var passwordInput: WebElement

    @FindBy(className = "login-form-submit")
    lateinit var submitButton: WebElement

    @FindBy(xpath = "//a[@href='/registration']")
    lateinit var notRegisteredButton: WebElement
}