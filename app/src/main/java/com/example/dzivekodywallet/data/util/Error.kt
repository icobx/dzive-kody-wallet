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

    ERROR_GENERATING_ACCOUNT {
        override fun toString(): String {
            return "Unable to generate an account"
        }
    },

    ERROR_INVALID_SOURCE_ACCOUNT {
        override fun toString(): String {
            return "Invalid source account ID"
        }
    },

    ERROR_INVALID_DESTINATION_ACCOUNT {
        override fun toString(): String {
            return "Invalid destination account ID"
        }
    },

    ERROR_TRANSACTION_SUBMIT {
        override fun toString(): String {
            return "Error submitting a transaction"
        }
    },

    ERROR_WALLET_NOT_FOUND {
        override fun toString(): String {
            return "Unable to find a wallet"
        }
    },

    ERROR_BAD_PIN {
        override fun toString(): String {
            return "Bad PIN for secret key"
        }
    },

    ERROR_CUSTOM {
        override fun toString(): String {
            return "Custom error for development purposes"
        }
    }
}
