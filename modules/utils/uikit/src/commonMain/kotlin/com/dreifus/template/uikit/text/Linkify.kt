package com.dreifus.template.uikit.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.dreifus.template.uikit.style.AppTheme

private val urlRegex = Regex("""(?:https?://|www\.)\S+""", RegexOption.IGNORE_CASE)

// Punctuation that ends a sentence around a URL rather than belonging to it.
private const val TRAILING_PUNCTUATION = ".,;:!?)]}»›\"'"

/**
 * Turns plain text into an [AnnotatedString] where URLs (`https://…`, `http://…`, `www.…`) are
 * tappable links styled with the accent color. Taps are handled by `Text` itself and open the URL
 * via the platform `UriHandler`; taps outside links still reach the parent clickable.
 */
@Composable
fun String.linkified(): AnnotatedString {
    val linkStyles = TextLinkStyles(
        style = SpanStyle(
            color = AppTheme.colors.accentPrimary,
            textDecoration = TextDecoration.Underline,
        ),
    )
    return remember(this, linkStyles) { linkify(this, linkStyles) }
}

private fun linkify(text: String, linkStyles: TextLinkStyles): AnnotatedString =
    buildAnnotatedString {
        var last = 0
        for (match in urlRegex.findAll(text)) {
            val start = match.range.first
            val link = match.value.trimEnd { it in TRAILING_PUNCTUATION }
            if (link.isEmpty()) continue
            append(text.substring(last, start))
            val url = if (link.startsWith("www", ignoreCase = true)) "https://$link" else link
            withLink(LinkAnnotation.Url(url, linkStyles)) { append(link) }
            last = start + link.length
        }
        append(text.substring(last))
    }
