package com.github.hirokazumiyaji.http

import okhttp3.Response

class HttpException : Exception {
    var response: Response? = null

    constructor(response: Response) : super() {
        this.response = response
    }
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(message: String, response: Response? = null) : super(message) {
        this.response = response
    }
}