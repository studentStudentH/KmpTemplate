package com.example.kmptemplate.domainmodel

data class FeeCategoryCollection @Throws(IllegalArgumentException::class) constructor(
    val value: List<FeeCategory>
) {
    init {
        if (value.isEmpty()) {
            throw IllegalArgumentException("FeeCategoryCollectionには最低１つはfeeCategoryは与えてください")
        }
        val namesNum = value.map { it.name }.toSet().size
        if (namesNum != value.size) {
            throw IllegalArgumentException(
                "カテゴリ名はすべて異なる必要があります"
            )
        }
    }

    /**
     * カテゴリをMost Recently Usedの降順で返す
     */
    fun getMostRecentlyUsedList(): List<FeeCategory> {
        return value.sortedByDescending { it.lastUsedAt }
    }

    fun hasSameName(newName: String): Boolean {
       return value.any { it.name == newName }
    }
}
