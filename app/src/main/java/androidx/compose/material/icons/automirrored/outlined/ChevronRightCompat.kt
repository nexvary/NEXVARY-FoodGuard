@file:Suppress("PackageDirectoryMismatch")

package androidx.compose.material.icons.automirrored.outlined

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Compatibility shim for Compose Material Icons versions that do not yet ship
 * AutoMirrored.Outlined.ChevronRight. The RC UI imports this symbol so it can
 * be upgraded transparently when the upstream icon becomes available.
 */
val Icons.AutoMirrored.Outlined.ChevronRight: ImageVector
    get() = Icons.Outlined.ChevronRight
