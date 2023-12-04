package com.example.calitour.model.repository

import android.util.Log
import com.example.calitour.model.DTO.BadgeDTO
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.DTO.ItineraryDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ItineraryRepository {

    suspend fun getItineraryByDate(userId:String, day: String):ArrayList<ItineraryDTO>{
        val result = Firebase.firestore.collection("users").document(userId).collection("itinerary")
            .whereEqualTo("day", day)
            .get().await()

        val itineraries = result.documents
        var itineraryArraylist = ArrayList<ItineraryDTO>();
        itineraries.forEach{
            val itinerary = it.toObject(ItineraryDTO::class.java)
            itinerary?.let {
                itineraryArraylist.add(itinerary)
            }
        }
        return itineraryArraylist
    }

    suspend fun findItineraryIdByEventId(userId: String, eventId: String): String {
        val result = Firebase.firestore.collection("users").document(userId).collection("itinerary")
            .get().await()

        val itineraries = result.documents
        var foundItineraryId=""

        for (itineraryDocument in itineraries) {
            val itinerary = itineraryDocument.toObject(ItineraryDTO::class.java)
            itinerary?.let {
                Log.i("Arreglo de eventos", itinerary.events.toString())
                Log.i("id del evento", eventId)
                // Verificar si el nombre del evento está presente en el arreglo de eventos del itinerario
                if (it.events.any { event -> event == eventId }) {
                    foundItineraryId = itineraryDocument.id
                    Log.i("Evento Encontrado", foundItineraryId!!)
                    return foundItineraryId
                }
            }
        }

        return foundItineraryId
    }

    suspend fun removeEventFromItinerary(userId: String, itineraryId: String, eventId: String) {
        val itineraryRef = Firebase.firestore.collection("users").document(userId).collection("itinerary")
            .document(itineraryId)

        try {
            val itinerarySnapshot = itineraryRef.get().await()
            val itinerary = itinerarySnapshot.toObject(ItineraryDTO::class.java)

            if (itinerary != null) {
                itinerary.events.remove(eventId)

                if (itinerary.events.isEmpty()) {
                    itineraryRef.delete().await()
                    Log.i("Itinerario Eliminado", "Se eliminó el itinerario $itineraryId ya que no tiene eventos.")
                } else {
                    itineraryRef.set(itinerary).await()
                    Log.i("Evento Eliminado", "Se eliminó el evento $eventId del itinerario $itineraryId")
                }
            } else {
                Log.e("Error", "No se encontró el itinerario con ID $itineraryId")
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al eliminar el evento: ${e.message}")
        }
    }


    suspend fun addEventToExistingItinerary(userId: String, itineraryId: String, eventId: String){
        val itineraryRef = Firebase.firestore.collection("users").document(userId).collection("itinerary")
            .document(itineraryId)

        try {
            val itinerarySnapshot = itineraryRef.get().await()
            val itinerary = itinerarySnapshot.toObject(ItineraryDTO::class.java)

            if (itinerary != null) {
                itinerary.events.add(eventId)

                itineraryRef.set(itinerary).await()

                Log.i("Evento Añadido", "Se Añadio el evento $eventId al itinerario $itineraryId")
            } else {
                Log.e("Error", "No se encontró el itinerario con ID $itineraryId")
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al añadir el evento: ${e.message}")
        }

    }

    suspend fun addEventToItineraryNonExisting(userId: String, day: String, eventId: String) {
        val itineraryRef = Firebase.firestore.collection("users").document(userId).collection("itinerary")

        try {
            val intineraryId = UUID.randomUUID().toString()
            val newItinerary = hashMapOf(
                "day" to day,
                "events" to arrayListOf(eventId),
                "id" to intineraryId
            )

            val result = itineraryRef.add(newItinerary).await()

            Log.i("Nuevo Itinerario", "Itinerario creado con ID: ${result.id}")

        } catch (e: Exception) {
            Log.e("Error", "Error al agregar evento al itinerario: ${e.message}")
        }
    }

}
