package com.service.alarmgeneratorservice.app

import com.service.alarmgeneratorservice.view.AlarmView
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.ConfigurableApplicationContext
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import tornadofx.View
import kotlin.reflect.KClass

@SpringBootApplication(
    exclude = [DataSourceAutoConfiguration::class],
    scanBasePackages = ["com.service.alarmgeneratorservice"]
)
open class AlarmApplication : App(RootView::class) {

    private lateinit var context: ConfigurableApplicationContext

    override fun init() {
        super.init()
        context = SpringApplication.run(AlarmApplication::class.java)
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>): T = context.getBean(type.java)
            override fun <T : Any> getInstance(type: KClass<T>, name: String): T = context.getBean(type.java, name)
        }
    }

    override fun stop() {
        super.stop()
        context.close()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(AlarmApplication::class.java, *args)
        }

        class RootView : View("Alarm Generator") {

            private val alarmView = find(AlarmView::class)

            override val root = alarmView.root
        }
    }
}