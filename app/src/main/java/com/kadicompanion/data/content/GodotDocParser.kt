package com.kadicompanion.data.content

import com.kadicompanion.domain.model.DocMember
import com.kadicompanion.domain.model.GodotDocEntry

object GodotDocParser {
    // Match YAML frontmatter: --- ... --- then body. The body keeps newlines.
    private val frontMatterRegex = Regex("^---\\s*\\n(.*?)\\n---\\s*\\n(.*)", RegexOption.DOT_MATCHES_ALL)
    private val signatureRegex = Regex("(?:([A-Za-z_][\\w<>]*)\\s+)?([A-Za-z_]\\w*)\\s*\\(")

    fun parse(source: GodotDocSource, defaultVerified: String): GodotDocEntry {
        val match = frontMatterRegex.find(source.markdown)
        val frontMatterRaw = match?.groupValues?.get(1) ?: ""
        val body = match?.groupValues?.get(2) ?: source.markdown

        val front = parseFrontMatter(frontMatterRaw)
        val className = front["class"] ?: source.className
        val inherits = front["inherits"]?.takeIf { it.isNotBlank() && it.lowercase() != "null" }
        val verified = front["verified"] ?: defaultVerified
        val short = front["short"] ?: ""

        val sections = parseSections(body)
        val properties = sections["Properties"]?.let { parseMembers(it, propertiesSection = true) } ?: emptyList()
        val methods = sections["Methods"]?.let { parseMembers(it, propertiesSection = false) } ?: emptyList()
        val signals = sections["Signals"]?.let {
            parseMembers(it, propertiesSection = false, preserveSignature = true)
        } ?: emptyList()
        val description = sections["Description"]?.trim() ?: body.trim()

        return GodotDocEntry(
            className = className,
            inherits = inherits,
            shortDescription = short,
            properties = properties,
            methods = methods,
            signals = signals,
            bodyMarkdown = description,
            lastVerifiedAgainstGodotVersion = verified
        )
    }

    private fun parseFrontMatter(raw: String): Map<String, String> {
        return raw.lineSequence()
            .filter { it.isNotBlank() }
            .associate { line ->
                val idx = line.indexOf(':')
                if (idx == -1) line.trim() to ""
                else line.substring(0, idx).trim() to line.substring(idx + 1).trim()
            }
    }

    private fun parseSections(body: String): Map<String, String> {
        val out = mutableMapOf<String, String>()
        val current = StringBuilder()
        var currentHeading: String? = null
        fun flush() {
            if (currentHeading != null && current.isNotEmpty()) {
                out[currentHeading!!] = current.toString().trim()
            }
            current.clear()
        }
        for (line in body.lineSequence()) {
            if (line.startsWith("## ")) {
                flush()
                currentHeading = line.removePrefix("## ").trim()
            } else if (currentHeading != null) {
                current.append(line).append('\n')
            }
        }
        flush()
        return out
    }

    private fun parseMembers(
        sectionText: String,
        propertiesSection: Boolean,
        preserveSignature: Boolean = false
    ): List<DocMember> {
        return sectionText.lineSequence()
            .filter { it.trimStart().startsWith("- ") }
            .mapNotNull { line ->
                val body = line.trimStart().removePrefix("- ").trim()
                val backtickEnd = body.indexOf('`', 1)
                if (!body.startsWith('`') || backtickEnd == -1) return@mapNotNull null
                val signature = body.substring(1, backtickEnd)
                val rest = body.substring(backtickEnd + 1).trim()
                val restType = if (propertiesSection) {
                    Regex("^:\\s*`([^`]+)`").find(rest)?.groupValues?.get(1)
                        ?: Regex("^\\s*`([^`]+)`").find(rest)?.groupValues?.get(1)
                } else {
                    null
                }
                val description = if (restType != null) {
                    val dashIdx = rest.indexOf('\u2014')
                    if (dashIdx >= 0) rest.substring(dashIdx + 1).trim() else ""
                } else {
                    when {
                        rest.startsWith("\u2014") -> rest.removePrefix("\u2014").trim()
                        rest.startsWith("-") -> rest.removePrefix("-").trim()
                        rest.startsWith(":") -> rest.removePrefix(":").trim()
                        else -> rest
                    }
                }
                val (parsedName, type) = splitSignature(signature)
                val name = if (preserveSignature) signature else parsedName
                DocMember(name = name, type = restType ?: type, description = description)
            }
            .toList()
    }

    private fun splitSignature(sig: String): Pair<String, String?> {
        val methodMatch = signatureRegex.find(sig)
        if (sig.contains('(') && methodMatch != null) {
            val retType = methodMatch.groupValues[1]
            val name = methodMatch.groupValues[2]
            return name to retType.takeIf { it.isNotBlank() }
        }
        val colonIdx = sig.indexOf(':')
        return if (colonIdx != -1) {
            sig.substring(0, colonIdx).trim() to sig.substring(colonIdx + 1).trim().ifBlank { null }
        } else {
            val match = signatureRegex.find(sig)
            if (match != null) {
                val retType = match.groupValues[1]
                val name = match.groupValues[2]
                name to retType.takeIf { it.isNotBlank() }
            } else sig.trim() to null
        }
    }
}
