package com.kaspresso.components.pageobjectcodegen

data class View(
    override val resourceId: String,
    override val viewType: String,
    override val packages: String,
) : BaseView {

    override fun toKaspressoExpression(): String {
        return "val ${resourceId.toCamelCase()} = K$viewType { withId(R.id.$resourceId) }"
    }
}

data class RecyclerView(
    override val resourceId: String,
    override val viewType: String,
    override val packages: String,
    val childView: Set<List<BaseView>>,
) : BaseView {

    val childClassNames = List(childView.size) { if (it == 0) "RecyclerViewItem" else "RecyclerViewItem$it" }

    override fun toKaspressoExpression(): String {
        return """val ${resourceId.toCamelCase()} = KRecyclerView(
        builder = { withId(R.id.$resourceId) },
        itemTypeBuilder = { ${childClassNames.joinToString(separator = ",\n" + "\t".repeat(7)) { "itemType(::$it)" }} },
    )"""
    }
}

interface BaseView {

    val resourceId: String
    val viewType: String
    val packages: String
    fun toKaspressoExpression(): String

    fun String.toCamelCase() = replace("_[a-z]".toRegex()) { it.value.last().uppercase() }
}
