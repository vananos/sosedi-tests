package io.github.vananos.sosedi.utils

import io.qameta.allure.listener.StepLifecycleListener
import io.qameta.allure.model.StepResult
import org.slf4j.LoggerFactory

class StepListener : StepLifecycleListener {
    val logger = LoggerFactory.getLogger(StepListener::class.java)

    override fun afterStepStop(result: StepResult?) {
    }
}