package com.example.eventhub.navigation

sealed class screen(val route :String){
    object onboard : screen("onboard")
    object login : screen("login")
    object signup : screen("signup")
    object home : screen("home")
    object explore : screen("explore")
    object messages : screen("messages")

    object profile : screen("profile")
    object Splash : screen("Splash")
    object editprofile : screen("editprofile")
    object becomevendor : screen("becomevendor")
    object myevents : screen("myevents")
    object createevent : screen("createevent?eventId={eventId}") {
        fun createRoute(eventId: String) = "createevent?eventId=$eventId"
        val baseRoute = "createevent"
    }
    object mybusiness : screen("mybusiness")
    object eventdetails : screen("eventdetail/{eventId}") {
        fun createRoute(eventId: String) = "eventdetail/$eventId" }
    object vendordetails : screen("vendordetail/{vendorId}") {
        fun createRoute(vendorId: String) = "vendordetail/$vendorId"
    }

    object chat {

        const val route = "chat/{chatId}"

        fun createRoute(chatId: String): String {
            return "chat/$chatId"
        }
    }
}
