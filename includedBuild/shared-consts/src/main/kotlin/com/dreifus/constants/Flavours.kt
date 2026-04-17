package com.dreifus.constants

object Flavours {
    val byLocation = Location
    val byStaging = Staging

    object Location {
        const val dimension = "location"

        const val global = "global"
    }

    object Staging {
        const val dimension = "staging"

        const val stage = "stage"
        const val prod = "prod"
    }
}
