package io.github.vananos.sosedi.steps

import io.qameta.allure.Step
import org.openqa.selenium.By
import kotlin.reflect.KClass

class UserSteps : BaseSteps() {
    private val stepsCache = mutableMapOf<KClass<*>, BaseSteps>()

    fun inAddressBar(action: AddressBarSteps.() -> Unit) = applyActionWithSteps(AddressBarSteps::class, action)

    fun stayingOnLoginPage(action: LoginPageSteps.() -> Unit) = applyActionWithSteps(LoginPageSteps::class, action)

    fun stayingOnRegistrationPage(action: RegistrationPageSteps.() -> Unit) =
        applyActionWithSteps(RegistrationPageSteps::class, action)

    fun stayingOnProfilePage(action: ProfilePageSteps.() -> Unit) =
        applyActionWithSteps(ProfilePageSteps::class, action)

    private fun <T : BaseSteps> applyActionWithSteps(clazz: KClass<T>, action: T.() -> Unit): UserSteps {
        getFromCache(clazz).apply(action)
        return this
    }

    private fun <T : BaseSteps> getFromCache(clazz: KClass<T>): T {
        if (!stepsCache.containsKey(clazz)) {
            stepsCache[clazz] = getInstance(clazz, driver)
        }
        return stepsCache[clazz] as T
    }

    @Step("click on navbar toggle")
    fun clickOnNabBarToggle() {
        driver.findElement(By.className("nav-toggler")).click()
        takeScreenShot()
    }

    @Step("wait for navbarexpanded")
    fun waitForNavBarExpanded() {
        waitFor(1, "navbar is expanded") {
            driver.findElements(By.className("nav-content-expanded")).isNotEmpty()
        }
        takeScreenShot()
    }

    @Step("click on nav item {item}")
    fun clickOnNavItem(item: NavLinks) {
        driver.findElements(By.xpath("//a[contains(@href, '$item')]"))
            .first()
            .click()
        Thread.sleep(500)
        takeScreenShot()
    }

}

enum class NavLinks {
    CREATE_AD, PROFILE, SETTINGS;

    override fun toString(): String {
        return super.toString().toLowerCase().replace("_", "-")
    }
}