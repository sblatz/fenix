/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.mozilla.fenix.ext

import android.content.Context
import mozilla.components.lib.publicsuffixlist.PublicSuffixList
import java.net.MalformedURLException
import java.net.URL

/**
 * Replaces the keys with the values with the map provided.
 */
fun String.replace(pairs: Map<String, String>): String {
    var result = this
    pairs.forEach { (l, r) -> result = result.replace(l, r) }
    return result
}

/**
 * Try to parse and get host part if this [String] is valid URL.
 * Returns **null** otherwise.
 */
fun String?.getHostFromUrl(): String? = try {
    URL(this).host
} catch (e: MalformedURLException) {
    null
}

suspend fun String.urlToTrimmedHost(context: Context): String {
    return PublicSuffixList(context).stripPublicSuffix(URL(this).host).await().removePrefix("www.")
}
