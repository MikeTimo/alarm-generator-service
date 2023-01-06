package com.service.alarmgeneratorservice.view

import com.service.alarmgeneratorservice.codec.BitBufferCodec
import com.service.alarmgeneratorservice.controller.AlarmController
import com.service.alarmgeneratorservice.domain.AlarmType
import javafx.application.Platform
import javafx.beans.property.SimpleSetProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableSet
import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tornadofx.*

class AlarmView : View() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val buttonList = mutableSetOf<ToggleButton>()

    private var street: TextField by singleAssign()
    private var house: TextField by singleAssign()

    companion object {
        const val ACTIVITY_BTN_HEIGHT = 30.0
        const val DEFAULT_BUTTON_WIDTH = 350.0
    }

    override val root = vbox {
        style {
            alignment = Pos.CENTER
            padding = box(5.px)
            spacing = 10.px
            minWidth = 900.px
            minHeight = 870.px
        }

        val codec = BitBufferCodec()
        val controller = AlarmController(codec)
        val observablePlaceSet: ObservableSet<AlarmType> = FXCollections.observableSet(mutableSetOf())
        val property = SimpleSetProperty(observablePlaceSet)

        add(createSendButton(property, controller))
        add(createAlarmGeneratorSituation(property, controller))
    }

    private fun createSendButton(
        property: SimpleSetProperty<AlarmType>,
        controller: AlarmController
    ) = hbox {
        style {
            alignment = Pos.TOP_CENTER
            spacing = 20.px
            padding = box(5.px)
        }

        try {
            add(createActionButton("Отправка", DEFAULT_BUTTON_WIDTH) {
                style {
                    alignment = Pos.TOP_CENTER
                    spacing = 20.px
                    padding = box(5.px)
                }
                setPlaceProperty(controller)
                controller.sendState()
            })

            add(createActionButton("Сброс параметров", DEFAULT_BUTTON_WIDTH) {
                style {
                    alignment = Pos.TOP_CENTER
                    spacing = 20.px
                    padding = box(5.px)
                }
                controller.reset()
                resetAlarmTypeButton(property)
                resetAddressProperty(controller)
            })
        } catch (e: Exception) {
            logger.error("Error: $e")
        }
    }

    private fun createAlarmGeneratorSituation(
        alarmProperty: SimpleSetProperty<AlarmType>,
        controller: AlarmController
    ) = hbox {

        style {
            alignment = Pos.CENTER
            spacing = 20.px
            padding = box(5.px)
        }

        add(createAlarmTypeView(alarmProperty, controller))
        add(createAddressView())
    }

    private fun createActionButton(
        name: String,
        elementWidth: Double,
        onChange: () -> Unit
    ): ToggleButton {
        val btn = ToggleButton(name)

        btn.minWidth = elementWidth
        btn.minHeight = ACTIVITY_BTN_HEIGHT
        btn.prefWidth = elementWidth
        btn.prefHeight = ACTIVITY_BTN_HEIGHT

        btn.setOnAction {
            Platform.runLater {
                btn.isSelected = false
                onChange()
            }
        }
        return btn
    }

    private fun setPlaceProperty(controller: AlarmController) {
        try {
            controller.street = street.text
            controller.house = house.text.toShort()
        } catch (e: NumberFormatException) {
            logger.error("initial place property error: $e")
        }
    }

    private fun createAlarmTypeView(
        property: SimpleSetProperty<AlarmType>,
        controller: AlarmController
    ) = vbox {

        style {
            alignment = Pos.CENTER
            padding = box(10.px)
            spacing = 10.px

            borderColor += box(Color.BLACK)
            borderRadius += box(5.px)
            borderWidth += box(1.px)
        }

        label("Причина тревоги") {
            style {
                fontWeight = FontWeight.BOLD
            }
        }

        add(createControl(AlarmType::values, property, DEFAULT_BUTTON_WIDTH, controller))
    }

    private fun createControl(
        values: () -> Array<AlarmType>,
        property: SimpleSetProperty<AlarmType>,
        elementWidth: Double,
        controller: AlarmController
    ) = vbox {
        style {
            alignment = Pos.CENTER
            spacing = 10.px
        }
        vbox {
            style {
                alignment = Pos.CENTER
            }
            values()
                .asIterable()
                .iterator()
                .forEach { type ->
                    add(createButton(property, type, elementWidth, controller))
                }
        }
    }

    private fun createButton(
        property: SimpleSetProperty<AlarmType>,
        type: AlarmType,
        elementWidth: Double,
        controller: AlarmController
    ): ToggleButton {
        val btn = ToggleButton(getNameByEnumType(type))

        btn.minWidth = elementWidth
        btn.minHeight = ACTIVITY_BTN_HEIGHT
        btn.prefWidth = elementWidth
        btn.prefHeight = ACTIVITY_BTN_HEIGHT

        btn.isSelected = property.value.equals(type)

        btn.setOnAction {
            Platform.runLater {
                if (property.contains(type)) {
                    property.remove(type)
                    btn.isSelected = false
                    controller.changeActivity(type, false)
                } else {
                    property.add(type)
                    btn.isSelected = true
                    controller.changeActivity(type, true)
                }
            }
        }

        buttonList.add(btn)
        return btn
    }

    private fun createAddressView() = vbox {
        style {
            alignment = Pos.CENTER
            padding = box(10.px)
            spacing = 10.px

            borderColor += box(Color.BLACK)
            borderRadius += box(5.px)
            borderWidth += box(1.px)
        }

        label("Параметры локации") {
            style {
                fontWeight = FontWeight.BOLD
            }
        }

        add(createAddressParamControl("Параметры"))
    }

    private fun createAddressParamControl(
        name: String,
    ) = vbox {
        style {
            alignment = Pos.CENTER
            spacing = 10.px
        }

        form {
            fieldset(name) {
                field("Название улицы") {
                    street = textfield()
                }
                field("Номер дома") {
                    house = textfield()
                }
            }
        }
    }

    private fun resetAlarmTypeButton(property: SimpleSetProperty<AlarmType>) {
        property.removeAll(AlarmType.values().toSet())
        buttonList.forEach { it.isSelected = false }
    }

    private fun resetAddressProperty(controller: AlarmController) {
        controller.street = ""
        controller.house = 0
        street.text = ""
        house.text = ""
    }

    private fun getNameByEnumType(type: AlarmType): String = type.name
}