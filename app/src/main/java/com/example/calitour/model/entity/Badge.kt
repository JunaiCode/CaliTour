package com.example.calitour.model.entity

import android.net.Uri
import java.util.UUID

data class Badge(
    var id: UUID,
    var img: Uri,
    var name: String
)