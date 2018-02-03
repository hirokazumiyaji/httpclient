package com.github.hirokazumiyaji.http

import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Test
import java.util.UUID

class ClientTest {
    private val client = Client(OkHttpClient())
    private val server = MockWebServer()

    @Test
    fun testGetOK() {
        var expected = "{\"id\":1000}"
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(expected)
        })

        var actual = client.get(server.url("").toString()).map {
            it.body()?.string() ?: ""
        }.block()
        Assert.assertEquals(expected, actual)

        expected = "[{\"id\":1000},{\"id\":2000}]"
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(expected)
        })
        client.get(server.url("").toString()).map {
            it.body()?.string() ?: ""
        }.block()

        expected = "{\"id\":1000}"
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(expected)
        })

        actual = client.get(
            server.url("").toString(),
            Headers.of(mapOf("Authorization" to UUID.randomUUID().toString()))
        ).map {
            it.body()?.string() ?: ""
        }.block()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testGetNG() {
        val body = "{\"id\":1000}"
        server.enqueue(MockResponse().apply {
            setResponseCode(400)
            setBody(body)
        })

        try {
            client.get(server.url("").toString()).block()
        } catch (e: Exception) {
            Assert.assertTrue(e.cause!! is HttpException)
            val exception = e.cause!! as HttpException
            Assert.assertEquals(body, exception.response?.body()?.string() ?: "")
        }
    }

    @Test
    fun testPost() {
        val body = "{\"id\":1000}"
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(body)
        })

        var actual = client.post(
            server.url("").toString(),
            RequestBody.create(MediaType.parse("application/json"), "{}")
        ).map {
           it.body()?.string() ?: ""
        }.block()
        Assert.assertEquals(body, actual)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(body)
        })

        actual = client.post(
            server.url("").toString(),
            RequestBody.create(MediaType.parse("application/json"), "{}"),
            Headers.of(mapOf("Authorization" to UUID.randomUUID().toString()))
        ).map {
            it.body()?.string() ?: ""
        }.block()
        Assert.assertEquals(body, actual)
    }

    @Test
    fun testPut() {
        val body = "{\"id\":1000}"
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(body)
        })

        var actual = client.put(
            server.url("").toString(),
            RequestBody.create(MediaType.parse("application/json"), "{}")
        ).map {
            it.body()?.string() ?: ""
        }.block()
        Assert.assertEquals(body, actual)

                server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(body)
        })

        actual = client.put(
            server.url("").toString(),
            RequestBody.create(MediaType.parse("application/json"), "{}"),
            Headers.of(mapOf("Authorization" to UUID.randomUUID().toString()))
        ).map {
            it.body()?.string() ?: ""
        }.block()
        Assert.assertEquals(body, actual)
    }

    @Test
    fun testPatch() {
        val body = "{\"id\":1000}"
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(body)
        })

        var actual = client.patch(
            server.url("").toString(),
            RequestBody.create(MediaType.parse("application/json"), "{}")
        ).map {
            it.body()?.string() ?: ""
        }.block()
        Assert.assertEquals(body, actual)

        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(body)
        })

        actual = client.patch(
            server.url("").toString(),
            RequestBody.create(MediaType.parse("application/json"), "{}"),
            Headers.of(mapOf("Authorization" to UUID.randomUUID().toString()))
        ).map {
            it.body()?.string() ?: ""
        }.block()
        Assert.assertEquals(body, actual)
    }

    @Test
    fun testDelete() {
        server.enqueue(MockResponse().apply {
            setResponseCode(204)
        })

        var actual = client.delete(server.url("").toString()).map {
            it.body()?.string()
        }.block()
        Assert.assertEquals("", actual)

                server.enqueue(MockResponse().apply {
            setResponseCode(204)
        })

        server.enqueue(MockResponse().apply {
            setResponseCode(204)
        })
        actual = client.delete(
            server.url("").toString(),
            headers = Headers.of(mapOf("Authorization" to UUID.randomUUID().toString()))
        ).map {
            it.body()?.string()
        }.block()
        Assert.assertEquals("", actual)
    }
}