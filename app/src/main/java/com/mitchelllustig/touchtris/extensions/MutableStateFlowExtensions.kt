package com.mitchelllustig.touchtris.extensions

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.asSetter(): (T) -> Unit {
    return { newValue -> this.value = newValue }
}
