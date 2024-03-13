package com.jetbrains.kmpapp.screens.texttocompose

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

data object TextToComposeScreen : Screen {
    @Composable
    override fun Content() {
        val text = """
            [
              {
                "type": "Column",
                "children": [
                  {
                    "type": "Text",
                    "text": "Hello, World!"
                  },
                  {
                    "type": "Button",
                    "child": {
                        "type": "Text",
                        "text": "Button"
                    } 
                  }
                ]
              }
            ]
        """.trimIndent()
        val screenModel = getScreenModel<TextToComposeScreenModel>()
        screenModel.ComposeFrom(text)
    }
}