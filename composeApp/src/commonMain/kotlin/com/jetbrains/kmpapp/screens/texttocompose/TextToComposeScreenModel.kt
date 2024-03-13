package com.jetbrains.kmpapp.screens.texttocompose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class TextToComposeScreenModel : ScreenModel {
    @Composable
    fun ComposeFrom(text: String) {
        val uiElements = parseUIElements(text)
        LazyColumn {
            items(uiElements) { uiElement ->
                ComposeFrom(uiElement)
            }
        }
    }

    @Composable
    private fun ComposeFrom(uiElement: UIElement) {
        val uiElementType = when (uiElement.type) {
            "Text" -> UIElementType.Text
            "Column" -> UIElementType.Column
            "Button" -> UIElementType.Button
            else -> UIElementType.Unknown
        }
        when (uiElementType) {
            is UIElementType.Text -> {
                Text(uiElement.text ?: "")
            }

            is UIElementType.Column -> {
                Column {
                    val children = uiElement.children
                    children?.forEach {
                        ComposeFrom(it)
                    }
                }
            }

            is UIElementType.Button -> {
                Button(onClick = {}) {
                    uiElement.child?.let { ComposeFrom(it) }
                }
            }

            UIElementType.Unknown -> Text("Unknown element type: ${uiElement.type}")
        }
    }

    private fun parseUIElements(text: String): List<UIElement> =
        Json.decodeFromString<List<UIElement>>(text)
}

@Serializable
sealed interface UIElementType {
    data object Column : UIElementType
    data object Text : UIElementType
    data object Button : UIElementType
    data object Unknown : UIElementType
}

@Serializable
data class UIElement(
    val type: String,
    val children: List<UIElement>? = emptyList(),
    val child: UIElement? = null,
    val text: String? = null,
)