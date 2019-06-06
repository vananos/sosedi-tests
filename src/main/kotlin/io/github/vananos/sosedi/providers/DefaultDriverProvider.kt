package io.github.vananos.sosedi.providers

import io.github.vananos.sosedi.ConfigProperties
import io.github.vananos.sosedi.Configuration
import org.openqa.selenium.Dimension
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

class DefaultDriverProvider : DriverProvider {
    override fun provide(): RemoteWebDriver {
        val browser = DesiredCapabilities().apply {
            browserName = Configuration[ConfigProperties.BROWSER_NAME]
            Configuration[ConfigProperties.BROWSER_VERSION].let {
                version = it
            }
        }
        val driver = RemoteWebDriver(URL(Configuration[ConfigProperties.SELENIUM_HOST]), browser)
        driver.manage().window().size = Dimension(
            Integer.parseInt(Configuration[ConfigProperties.BROWSER_WIDTH]),
            Integer.parseInt(Configuration[ConfigProperties.BROWSER_HEIGHT])
        )
        return driver
    }

    override fun isProviderForName(provideName: String): Boolean = true
}