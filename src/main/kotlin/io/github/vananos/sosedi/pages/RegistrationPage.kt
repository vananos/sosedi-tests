package io.github.vananos.sosedi.pages

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy

class RegistrationPage {

    @FindBy(name = "name")
    lateinit var nameInput: WebElement

    @FindBy(name = "surname")
    lateinit var surnameInput: WebElement

    @FindBy(name = "email")
    lateinit var emailInput: WebElement

    @FindBy(name = "password")
    lateinit var passwordInput: WebElement

    @FindBy(name = "passwordConfirmation")
    lateinit var passwordConfirmationInput: WebElement

    @FindBy(className = "registration-form-submit-btn")
    lateinit var submitFormButton: WebElement
}