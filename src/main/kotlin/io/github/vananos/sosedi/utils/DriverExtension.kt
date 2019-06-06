package io.github.vananos.sosedi.utils

import io.github.vananos.sosedi.ConfigProperties
import io.github.vananos.sosedi.Configuration
import io.github.vananos.sosedi.providers.DefaultDriverProvider
import io.github.vananos.sosedi.providers.DriverProvider
import io.github.vananos.sosedi.steps.UserSteps
import io.github.vananos.sosedi.steps.getInstance
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import java.util.*

class DriverExtension : ParameterResolver, AfterEachCallback {
    private val logger = LoggerFactory.getLogger(DriverExtension::class.java)
    private lateinit var driver: WebDriver

    override fun afterEach(context: ExtensionContext?) {
        logger.info("closing driver")
        driver.close()
    }

    override fun supportsParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Boolean {
        return parameterContext!!.parameter.type == UserSteps::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Any {
        driver = getDriver()

        logger.info("inject driver")
        return getInstance(UserSteps::class, driver)
    }

    private fun getDriver(): RemoteWebDriver {

        val driverProviderName = Configuration[ConfigProperties.DRIVER_PROVIDER]

        val driverProvider = if (driverProviderName.isNotBlank()) {
            ServiceLoader.load(DriverProvider::class.java)
                .find {
                    it.isProviderForName(driverProviderName)
                } ?: throw IllegalStateException("provider for name $driverProviderName not found")
        } else {
            DefaultDriverProvider()
        }

        return driverProvider.provide()
    }
}