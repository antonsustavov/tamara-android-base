package com.tamara.care.watch.data.entity

data class ChatEntity(
    var text: String? = "",
    var image: String? = "",

    var type: ChatType,
    var time: String? = "",
) {

}

enum class ChatType(val type: String) {
    USER_TEXT("USER_TEXT"),
    OPPONENT_TEXT("OPPONENT_TEXT")
}