package io.github.vananos.sosedi.pages

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy

class ProfilePage {

    @FindBy(name = "name")
    lateinit var nameInput: WebElement

    @FindBy(name = "surname")
    lateinit var sunameInput: WebElement

    @FindBy(name = "birthday")
    lateinit var birthdayInput: WebElement

    @FindBy(name = "phone")
    lateinit var phoneInput: WebElement

    @FindBy(xpath = "//input[contains(@name, 'like-')]/..")
    lateinit var interestCheckboxes: MutableList<WebElement>

    @FindBy(name = "description")
    lateinit var descriptionInput: WebElement

    @FindBy(xpath = "//button")
    lateinit var saveButton: WebElement
}