package com.example.shrimpcaring.ble

class RelayManager(

    private val writer: BleWriter

) {

    //--------------------------------------------------------
    // Single Relay
    //--------------------------------------------------------

    fun turnOn(

        relayNumber: Int

    ): Boolean {

        return writer.sendRelayCommand(

            relayNumber,

            true

        )

    }

    fun turnOff(

        relayNumber: Int

    ): Boolean {

        return writer.sendRelayCommand(

            relayNumber,

            false

        )

    }

    fun toggle(

        relayNumber: Int,

        currentState: Boolean

    ): Boolean {

        return writer.sendRelayCommand(

            relayNumber,

            !currentState

        )

    }

    //--------------------------------------------------------
    // All Relays
    //--------------------------------------------------------

    fun turnAllOn(

        totalRelays: Int

    ) {

        for (relay in 1..totalRelays) {

            writer.sendRelayCommand(

                relay,

                true

            )

            Thread.sleep(100)

        }

    }

    fun turnAllOff(

        totalRelays: Int

    ) {

        for (relay in 1..totalRelays) {

            writer.sendRelayCommand(

                relay,

                false

            )

            Thread.sleep(100)

        }

    }

}