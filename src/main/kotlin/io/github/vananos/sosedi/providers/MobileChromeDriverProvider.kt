package io.github.vananos.sosedi.providers

import io.github.vananos.sosedi.ConfigProperties
import io.github.vananos.sosedi.Configuration
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

class MobileChromeDriverProvider : DriverProvider {
    override fun isProviderForName(providerName: String): Boolean {
        return "iphone5" == providerName
    }

    override fun provide(): RemoteWebDriver {
        val settings = mapOf("deviceName" to "iPhone 5/SE")
        return RemoteWebDriver(URL(Configuration[ConfigProperties.SELENIUM_HOST]), ChromeOptions().apply {
            setExperimentalOption("mobileEmulation", settings)
        })
    }
}