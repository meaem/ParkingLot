package parking

class Car(val licenceNo: String, val color: String) {
    override fun toString(): String {
        return "$licenceNo $color"
    }
}

class ParkingLot() {
    var lots = mutableListOf<Car?>()

    fun initLot(capacity: Int): Boolean {
        lots = MutableList(capacity) { null }
        return true
    }

    fun park(car: Car): Int {
        checkInitialized()
        val index = getFirstFreeLot()
        if (index == -1) {
            throw IllegalArgumentException("Sorry, the parking lot is full.")
        }
        lots[index] = car
        return index + 1
    }

    fun leave(position: Int): Car {
        checkInitialized()
        if (position !in 1..lots.size) {
            throw java.lang.IllegalArgumentException("Invalid Position")
        }

        if (lots[position - 1] == null) {
            throw java.lang.IllegalArgumentException("There is no car in spot $position.")
        }
        val c = lots[position - 1]
        lots[position - 1] = null
        return c!!
    }

    private fun getFirstFreeLot(): Int {
        checkInitialized()
        for (l in lots.indices) {
            if (lots[l] == null) return l
        }
        return -1
    }

    fun getStatus(): List<Pair<Int, Car>> {
        checkInitialized()
        return lots.mapIndexed { index, car -> index + 1 to car }
            .filter { it.second != null }
            .map { it.first to it.second!! }
    }

    private fun checkInitialized() {
        if (lots.isEmpty()) {
            throw IllegalArgumentException("Sorry, a parking lot has not been created.")
        }
    }
}

fun main() {
    val parking = ParkingLot()

    val cmdRegex = "\\s+".toRegex()
    var cmd = readln()
    while (true) {
        try {
            val tokens = cmd.split(cmdRegex)
            if (tokens.size == 3 && tokens[0] == "park") {
                val lic = tokens[1]
                val colr = tokens[2]
                val freeLot = parking.park(Car(lic, colr))
                println("$colr car parked in spot $freeLot.")
            } else if (tokens.size == 2 && tokens[0] == "leave") {
                val position = tokens[1].toInt()
                    parking.leave(position)
                    println("Spot $position is free.")
            } else if (tokens.size == 2 && tokens[0] == "create") {
                val capacity = tokens[1].toInt()
                if (parking.initLot(capacity)) {
                    println("Created a parking lot with $capacity spots.")
                }
            } else if (tokens[0] == "status") {
                val status = parking.getStatus()
                if (status.isNotEmpty()) {
                    for (s in status) {
                        println("${s.first} ${s.second}")
                    }
                } else {
                    println("Parking lot is empty.")
                }
            } else if (tokens[0] == "exit") {
                break
            } else {
                println("Invalid command.")
            }
        } catch (ex:IllegalArgumentException) {
            println(ex.message)
        }
        cmd = readln()
    }
}


