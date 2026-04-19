package com.dreifus.template.uikit.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreifus.template.uikit.preview.AppPreview
import com.dreifus.template.uikit.style.AppIcon
import com.dreifus.template.uikit.style.AppTheme
import kotlin.math.roundToInt

@Suppress("ComplexMethod", "CyclomaticComplexMethod")
@Composable
fun AppTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    errorText: String = "",
    isEnabled: Boolean = true,
    leadingIcon: AppIcon? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    testTag: String = "",
    errorTestTag: String = "",
    isRequired: Boolean = false,
    autofillType: ContentType? = null,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isError = errorText.isNotEmpty()
    val borderColor = when {
        isError -> AppTheme.colors.inputBorderError
        isFocused -> AppTheme.colors.inputBorderFocus
        else -> Color.Transparent
    }
    val backgroundColor = AppTheme.colors.bgCardPrimary
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(64.dp))
                .background(backgroundColor)
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(64.dp)),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val isPasswordVisualTransformation =
                    visualTransformation is PasswordVisualTransformation
                val hidePassword = remember { mutableStateOf(isPasswordVisualTransformation) }
                val typography = AppTheme.typography
                val height = calculateTextFieldHeight()
                MaterialTheme(
                    // заменяем шрифты в MaterialTheme на наши дабы анимация шла по их размерам
                    typography = MaterialTheme.typography.copy(
                        bodySmall = typography.bodyMedium,
                        bodyLarge = typography.bodyLarge,
                    ),
                ) {
                    TextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .semantics {
                                autofillType?.let { contentType = it }
                            }
                            .testTag(testTag)
                            .height(height)
                            .padding(horizontal = 6.dp)
                            .weight(1f),
                        leadingIcon = leadingIcon?.let { icon ->
                            { icon(tint = AppTheme.colors.inputTextDefault) }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = AppTheme.colors.inputTextFilled,
                            unfocusedTextColor = AppTheme.colors.inputTextFilled,
                            disabledTextColor = AppTheme.colors.inputTextFilled,
                            errorTextColor = AppTheme.colors.inputTextFilled,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            cursorColor = AppTheme.colors.inputBorderFocus,
                            errorCursorColor = AppTheme.colors.inputTextError,
                        ),
                        shape = RoundedCornerShape(64.dp),
                        singleLine = true,
                        textStyle = typography.bodyMedium,
                        label = {
                            Text(
                                text = labelText.withRequiredMark(isRequired),
                                style = AppTheme.typography.bodyMedium,
                                color = if (isError) {
                                    AppTheme.colors.inputTextError
                                } else {
                                    AppTheme.colors.inputTextDefault
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        isError = isError,
                        enabled = isEnabled,
                        interactionSource = interactionSource,
                        visualTransformation = when {
                            isPasswordVisualTransformation && !hidePassword.value -> VisualTransformation.None
                            isPasswordVisualTransformation && hidePassword.value -> visualTransformation
                            else -> visualTransformation
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = imeAction,
                            keyboardType = keyboardType,
                        ),
                        keyboardActions = keyboardActions,
                    )
                }
            }
        }
        CaptionAndError(
            isError = isError,
            isEnabled = isEnabled,
            errorText = errorText,
            errorTestTag = errorTestTag,
        )
    }
}

@Composable
private fun calculateTextFieldHeight(): Dp {
    val typography = AppTheme.typography
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    return remember(textMeasurer, density, typography) {
        // Вычисляем высоту текста ввода и подсказки,
        val textHeight = textMeasurer.measure("", typography.bodyLarge)
            .multiParagraph.height.roundToInt()
        val labelHeight = textMeasurer.measure("", typography.bodyMedium)
            .multiParagraph.height.roundToInt()
        with(density) {
            val verticalPaddings = 8.dp.roundToPx() * 2
            maxOf(
                TextFieldDefaults.MinHeight.roundToPx(),
                verticalPaddings + labelHeight + textHeight,
            ).toDp()
        }
    }
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String,
    errorText: String = "",
    isEnabled: Boolean = true,
    leadingIcon: AppIcon? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    testTag: String = "",
    errorTestTag: String = "",
    initialSelection: TextRange = TextRange.Zero,
    isRequired: Boolean = false,
    autofillType: ContentType? = null,
) {
    // Holds the latest internal TextFieldValue state. We need to keep it to have the correct value
    // of the composition.
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = initialSelection
            )
        )
    }
    // Holds the latest TextFieldValue that BasicTextField was recomposed with. We couldn't simply
    // pass `TextFieldValue(text = value)` to the CoreTextField because we need to preserve the
    // composition.
    val textFieldValue = textFieldValueState.copy(text = value)

    SideEffect {
        if (textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }
    // Last String value that either text field was recomposed with or updated in the onValueChange
    // callback. We keep track of it to prevent calling onValueChange(String) for same String when
    // CoreTextField's onValueChange is called multiple times without recomposition in between.
    var lastTextValue by remember(value) { mutableStateOf(value) }

    AppTextField(
        value = textFieldValue,
        onValueChange = { newTextFieldValueState ->
            textFieldValueState = newTextFieldValueState

            val stringChangedSinceLastInvocation = lastTextValue != newTextFieldValueState.text
            lastTextValue = newTextFieldValueState.text

            if (stringChangedSinceLastInvocation) {
                onValueChange(newTextFieldValueState.text)
            }
        },
        modifier = modifier,
        labelText = labelText,
        errorText = errorText,
        isEnabled = isEnabled,
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        imeAction = imeAction,
        keyboardType = keyboardType,
        keyboardActions = keyboardActions,
        testTag = testTag,
        errorTestTag = errorTestTag,
        isRequired = isRequired,
        autofillType = autofillType,
    )
}

@Composable
private fun CaptionAndError(
    isError: Boolean,
    isEnabled: Boolean,
    errorText: String,
    errorTestTag: String,
) {
    AnimatedVisibility(visible = isError && isEnabled) {
        Text(
            modifier = Modifier
                .padding(top = 4.dp, start = 22.dp)
                .testTag(errorTestTag),
            text = errorText,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.inputTextError,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun CharSequence.withRequiredMark(isRequired: Boolean = true) = buildAnnotatedString {
    append(this@withRequiredMark)
    if (isRequired) {
        append('*')
        addStyle(SpanStyle(color = AppTheme.colors.inputTextError), length - 1, length)
    }
}

@Composable
private fun InputsPreview() {
    Column(modifier = Modifier.padding(12.dp)) {
        AppTextField(
            value = "",
            onValueChange = { },
            labelText = "Email",
        )

        AppTextField(
            value = "zxc@mail.com",
            onValueChange = { },
            modifier = Modifier.padding(top = 20.dp),
            labelText = "Email",
        )

        AppTextField(
            value = "Password",
            onValueChange = { },
            modifier = Modifier.padding(top = 20.dp),
            labelText = "Password",
            visualTransformation = PasswordVisualTransformation(),
        )

        AppTextField(
            value = "zxcq@mail.com",
            onValueChange = { },
            modifier = Modifier.padding(top = 20.dp),
            labelText = "Email",
            errorText = "Error",
        )

        AppTextField(
            value = "zxcq@mail.com",
            onValueChange = { },
            modifier = Modifier.padding(top = 20.dp),
            labelText = "Email",
        )

        AppTextField(
            value = "zxcq@mail.com",
            onValueChange = { },
            modifier = Modifier.padding(top = 20.dp),
            labelText = "Email",
            errorText = "Error",
        )

        AppTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier.padding(top = 20.dp),
            labelText = "Email",
            isEnabled = false,
        )

        AppTextField(
            value = "zxcq@mail.com",
            onValueChange = { },
            modifier = Modifier.padding(top = 20.dp),
            labelText = "Email",
            isEnabled = false,
        )
    }
}

@Preview(heightDp = 900)
@Composable
private fun PreviewLightTheme() {
    AppPreview {
        InputsPreview()
    }
}
