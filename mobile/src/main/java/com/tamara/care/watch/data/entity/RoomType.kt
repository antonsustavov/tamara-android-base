package com.tamara.care.watch.data.entity


enum class RoomType(val key: String) {
    BEDROOM("bedroom"),
    ENTER("enter"),
    BATHROOM("bathroom"),
    KITCHEN("kitchen"),
    TOILET("toilet"),
    GUESTROOM("guestroom"),
    INDICES("indices"),
    FINANCIAL("fifancial")
}

fun getRooms(): MutableList<RoomType> {
    return mutableListOf(
        RoomType.BEDROOM,
        RoomType.ENTER,
        RoomType.BATHROOM,
        RoomType.KITCHEN,
        RoomType.TOILET,
        RoomType.GUESTROOM
    )
}

data class AdapterRoomEntity(
    val name: String?,
    var isOccupied: Boolean = false
)