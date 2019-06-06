package io.github.vananos.sosedi

import java.util.*

object Configuration {
    private val properties = Properties()

    init {
        properties.load(Configuration::class.java.classLoader.getResourceAsStream("tests.properties"))
    }

    operator fun get(key: ConfigProperties) = properties.getProperty("tests.$key")

}

enum class ConfigProperties {
    STAGE, BROWSER_NAME, BROWSER_HEIGHT, BROWSER_WIDTH, BROWSER_VERSION, DRIVER_PROVIDER, SELENIUM_HOST;

    override fun toString() = super.toString().toLowerCase().replace("_", ".")
}