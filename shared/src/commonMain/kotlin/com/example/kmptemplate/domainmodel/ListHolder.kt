package com.example.kmptemplate.domainmodel

/**
 * Skieの制約で
 * - GenericsはAnyに準拠する
 * - Generic<List<A>>の代わりにGeneric<ListHolder<A>>とする
 * もしそうしないとSwiftに変換した時、List内部の型が失われてしまう
 */
data class ListHolder<T : Any>(val list: List<T>)
