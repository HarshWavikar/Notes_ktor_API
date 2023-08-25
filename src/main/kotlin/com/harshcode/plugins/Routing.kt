package com.harshcode.plugins

import com.harshcode.authentication.JWTService
import com.harshcode.authentication.hash
import com.harshcode.data.model.User
import com.harshcode.repository.Repo
import com.harshcode.route.NoteRoutes
import com.harshcode.route.UserRoute
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val db = Repo()
    val jwtService = JWTService()
    val hashFunction = { s:String -> hash(s) }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        UserRoute(db, jwtService, hashFunction)
        NoteRoutes(db, hashFunction)

        get("/") {
            call.respondText("Hello World Harsh Babu!")
        }

        get("/token") {
            //Here we have to get email, userName and password in path parameters
            val email = call.request.queryParameters["email"]!!
            val password = call.request.queryParameters["password"]!!
            val username = call.request.queryParameters["username"]!!

            val user = User(email = email, hashPassword = hashFunction(password), userName = username)
            call.respond(jwtService.generateToken(user))
        }

    }
}


