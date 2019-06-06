package io.github.vananos.sosedi.providers

import org.openqa.selenium.remote.RemoteWebDriver

interface DriverProvider {
    fun provide(): RemoteWebDriver
    fun isProviderForName(provideName: String): Boolean
}