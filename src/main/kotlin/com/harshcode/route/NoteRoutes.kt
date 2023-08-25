package com.harshcode.route

import com.harshcode.data.model.Note
import com.harshcode.data.model.SimpleResponse
import com.harshcode.data.model.User
import com.harshcode.repository.Repo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTE = "$NOTES/create"
const val UPDATE_NOTE = "$NOTES/update"
const val DELETE_NOTE = "$NOTES/delete"



fun Route.NoteRoutes(
    db : Repo,
    hashFunction: (String)->String
){

    //Now we have to authenticate all the note routes, to authenticate them:
    authenticate ("jwt"){
        // In this block we have to write all teh notes routes, it will automatically authenticate them

        post (CREATE_NOTE){
            // First of all we have to receive a note
            val note = try {
                call.receive<Note>()
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Fields"))
                return@post
            }

            try {
                // We have to get the email of the user, to get the email.
                //Type of principal is User
                val email = call.principal<User>()!!.email
                // Now we have to insert note in database
                db.addNote(note = note, email = email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true, "Note added successfully"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(true, e.message ?: "Some problem occurred"))
            }
        }

        get(NOTES) {
            try {
                // First get teh email of the user from principal
                val email = call.principal<User>()!!.email
                // Pass the email here
                val notes = db.getAllNotes(email)
                // Pass the notes ass response
                call.respond(HttpStatusCode.OK, notes)
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(true, emptyList<Note>().toString()))
            }
        }

        post(UPDATE_NOTE) {
            val note = try {
                call.receive<Note>()
            }catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Fields"))
                return@post
            }

            try {
                val email = call.principal<User>()!!.email
                db.updateNote(note, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Updated Successfully"))
            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(true, e.message ?: "Some problem occurred"))
            }
        }

        delete(DELETE_NOTE) {
            val noteId = try {
                call.request.queryParameters["id"]!!
            }catch (e:Exception){
                //If the user is not passing this id then exception will occur
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Query parameter: 'id' is not present"))
                return@delete
            }

            try {
                val email = call.principal<User>()!!.email
                db.deleteNote(noteId, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Deleted Successfully."))
            }catch (e: Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(true, e.message ?: "Some problem occurred"))
            }
        }


    }
}

















