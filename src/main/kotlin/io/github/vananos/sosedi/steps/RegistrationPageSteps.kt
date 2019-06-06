package io.github.vananos.sosedi.steps

import io.github.vananos.sosedi.data.RegistrationFormData
import io.github.vananos.sosedi.pages.RegistrationPage
import io.qameta.allure.Step

class RegistrationPageSteps(private val registrationPage: RegistrationPage) : BaseSteps() {

    @Step("fill registration form with data: {registrationFormData}")
    fun fillRegistrationForm(registrationFormData: RegistrationFormData) {
        fillInput(registrationPage.nameInput, registrationFormData.name)
        fillInput(registrationPage.surnameInput, registrationFormData.surname)
        fillInput(registrationPage.emailInput, registrationFormData.email)
        fillInput(registrationPage.passwordInput, registrationFormData.password)
        fillInput(registrationPage.passwordConfirmationInput, registrationFormData.passwordConfirmation)

        Thread.sleep(500)
        takeScreenShot()
    }

    fun clickOnSubmitFormData() = registrationPage.submitFormButton.click()
}