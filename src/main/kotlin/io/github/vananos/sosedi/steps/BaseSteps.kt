package io.github.vananos.sosedi.steps

import io.qameta.allure.Allure
import io.qameta.allure.Step
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait
import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.shooting.ShootingStrategies
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.jvmErasure

open class BaseSteps {
    lateinit var driver: WebDriver


    fun getErrors() = driver.findElements(By.className("error"))

    fun getNotifications() = driver.findElements(By.className("notification"))

    fun waitForPopup(time: Long) {
        waitFor(time, "popup") {
            driver.findElement(By.xpath("//div[@class='modal']"))
                .getAttribute("style")
                .contains("display: flex")
        }
    }

    fun waitForErrors(time: Long) {
        waitFor(time, "waiting for errors") {
            getErrors().isNotEmpty()
        }
    }

    @Step("waiting for {description} {time} s.")
    fun waitFor(time: Long, description: String = "", condition: BaseSteps.() -> Boolean) {
        try {
            WebDriverWait(driver, time).until { condition() }
        } finally {
            takeScreenShot()
        }
    }

    fun takeScreenShot() {
        val image = AShot().shootingStrategy(ShootingStrategies.viewportRetina(100, 0, 0, 2f))
            .takeScreenshot(driver).image
        val imageBytes = ByteArrayOutputStream()

        ImageIO.write(image, "png", imageBytes)

        Allure.addByteAttachmentAsync("Page screenshot", "image/png") {
            imageBytes.toByteArray()
        }
    }


    fun fillInput(element: WebElement, value: String?) {
        value.let {
            element.clear()
            element.sendKeys(it)
            fireOnChange(element)
        }
    }

    fun fireOnChange(element: WebElement) =
        (driver as JavascriptExecutor).executeScript("arguments[0].dispatchEvent(new Event('change'));", element)
}

@SuppressWarnings("unchecked")
fun <T : BaseSteps> getInstance(clazz: KClass<T>, driver: WebDriver): T {

    if (clazz.constructors.size != 1) {
        throw IllegalArgumentException("Ambiguous constructor in $clazz ")
    }

    val constructor = clazz.constructors.elementAt(0)

    var stepsInstance = if (constructor.parameters.isEmpty()) {
        constructor.call()
    } else {

        val constructorParameters = constructor.parameters.stream()
            .map { parameter ->
                val obj = parameter.type.jvmErasure.createInstance()

                PageFactory.initElements(driver, obj)
                obj
            }
            .toArray()

        constructor.call(*constructorParameters)
    }

    return stepsInstance.apply {
        this.driver = driver
    }
}