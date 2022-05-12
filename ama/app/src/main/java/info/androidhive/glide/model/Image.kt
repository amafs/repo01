package info.androidhive.glide.model

import java.io.Serializable

class Image : Serializable {
    var name: String? = null
    var small: String? = null
    var medium: String? = null
    var large: String? = null
    var timestamp: String? = null

    constructor() {}
    constructor(
        name: String?,
        small: String?,
        medium: String?,
        large: String?,
        timestamp: String?
    ) {
        this.name = name
        this.small = small
        this.medium = medium
        this.large = large
        this.timestamp = timestamp
    }
}