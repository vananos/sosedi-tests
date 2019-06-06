package io.github.vananos.sosedi.steps

import io.github.vananos.sosedi.data.ProfileFormData
import io.github.vananos.sosedi.pages.ProfilePage
import io.qameta.allure.Step
import java.lang.Boolean.parseBoolean

class ProfilePageSteps(private val profilePage: ProfilePage) : BaseSteps() {

    @Step("fill profile data")
    fun fillProfileForm(profileFormData: ProfileFormData) {
        fillInput(profilePage.nameInput, profileFormData.name)
        fillInput(profilePage.sunameInput, profileFormData.surname)
        fillInput(profilePage.birthdayInput, profileFormData.birthdate)
        fillInput(profilePage.phoneInput, profileFormData.phone)

        profilePage.interestCheckboxes.forEach {
            val mustBeChecked = profileFormData.interests.contains(it.getAttribute("value"))
            val isCheckedNow = parseBoolean(it.getAttribute("checked"))

            if (isCheckedNow xor mustBeChecked) {
                it.click()
            }
        }

        fillInput(profilePage.descriptionInput, profileFormData.description)
        takeScreenShot()
    }

    @Step("click on save button")
    fun clickOnSaveButton() {
        profilePage.saveButton.click()
        takeScreenShot()
    }
}
