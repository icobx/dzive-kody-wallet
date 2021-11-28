package com.example.dzivekodywallet.data.util

enum class Error {
    NO_ERROR {
        override fun toString(): String {
            return "No error occurred"
        }
    },

    ERROR_STELLAR {
        override fun toString(): String {
            return "Error with stellar"
        }
    },

    ERROR_OFFLINE {
        override fun toString(): String {
            return "No internet connection"
        }
    },

    ERROR_CUSTOM {
        override fun toString(): String {
            return "Custom error for development purposes"
        }
    }
}
