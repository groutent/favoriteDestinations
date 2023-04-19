package team.jot.favoriteplaces

import androidx.lifecycle.ViewModel

// This class extends from ViewModel
class DestinationViewModel: ViewModel() {
    var destinations = mutableListOf<Destination>()
    var destinationId = -1

    fun getId(): Int { return destinationId }
    fun setId(int: Int){ destinationId = int}

    fun getDestination(index: Int): Destination { return destinations[index]}
    fun setDestination(destination: Destination, index: Int){ destinations[index] = destination}
    fun removeDestination(index: Int){destinations.removeAt(index)}

    fun getAllDestinations(): MutableList<Destination> { return destinations}
    fun setAllRatings(newDestinations: MutableList<Destination>){ destinations = newDestinations}
}
