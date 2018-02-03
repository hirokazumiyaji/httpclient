package com.github.hirokazumiyaji.http

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import reactor.core.publisher.Mono

open class Client(private val http: OkHttpClient) {
    fun request(request: Request): Mono<Response> {
        return Mono.just(http.newCall(request).execute())
            .doOnError {
                throw HttpException(it)
            }.doOnSuccess {
                if (!it.isSuccessful) {
                    throw HttpException(it)
                }
            }
    }

    fun get(url: String, headers: Headers? = null): Mono<Response> {
        var req = Request.Builder()
            .url(url)
            .get()
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }

    fun post(url: String, body: RequestBody, headers: Headers? = null): Mono<Response> {
        var req = Request.Builder()
            .url(url)
            .post(body)
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }

    fun put(url: String, body: RequestBody, headers: Headers? = null): Mono<Response> {
        var req = Request.Builder()
            .url(url)
            .put(body)
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }

    fun patch(url: String, body: RequestBody, headers: Headers? = null): Mono<Response> {
        var req = Request.Builder()
            .url(url)
            .patch(body)
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }

    fun delete(url: String, body: RequestBody? = null, headers: Headers? = null): Mono<Response> {
        var req = Request.Builder()
            .url(url)
            .delete(body)
        headers?.let {
            req = req.headers(it)
        }
        return request(req.build())
    }
}
