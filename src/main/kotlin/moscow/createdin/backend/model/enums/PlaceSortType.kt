package moscow.createdin.backend.model.enums

enum class PlaceSortType(val columnName: String) {
    popular("popular"),
    personal("popular"),
    rating("rating"),
    price("min_price")
}
