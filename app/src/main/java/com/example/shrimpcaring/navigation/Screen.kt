package com.example.shrimpcaring.navigation

sealed class Screen(val route: String, val title: String) {

    object Home : Screen("home", "Shrimp Ponds")

    object DeviceSelection : Screen("devices/{pondId}", "Select Device") {

        fun createRoute(id: Int) =
            "devices/$id"

    }

    object AeratorCount : Screen("aeratorCount/{pondId}", "Aerators") {

        fun createRoute(id: Int) =
            "aeratorCount/$id"

    }

    object Aerator : Screen("aerator/{pondId}", "Aerator Module") {

        fun createRoute(id: Int) =
            "aerator/$id"

    }
    object Ph : Screen("ph/{pondId}", "pH Sensor") {
        fun createRoute(id: Int) = "ph/$id"
    }
    object Do : Screen("do/{pondId}", "DO Sensor") {
        fun createRoute(id: Int) = "do/$id"
    }

    object Temperature : Screen("temperature/{pondId}", "Temperature Sensor") {
        fun createRoute(id: Int) = "temperature/$id"
    }

    object Dashboard : Screen("dashboard", "Dashboard")
    object Control : Screen("control", "Control")
    object Logger : Screen("logger", "Data Logger")
    object History : Screen("history", "History")
    object Setup : Screen("setup", "Setup")
    object Settings : Screen("settings", "Settings")

}
