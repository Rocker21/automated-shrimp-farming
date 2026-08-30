package com.example.shrimpcaring.ble

class BleProvision(

    private val writer: BleWriter

) {

    var onStarted: (() -> Unit)? = null

    var onCompleted: (() -> Unit)? = null

    var onFailed: ((String) -> Unit)? = null

    fun provision(

        pondId: Int,

        ssid: String,

        password: String,

        deviceName: String,

        interval: Int

    ) {

        Thread {

            try {

                onStarted?.invoke()

                writer.sendPondId(pondId)
                Thread.sleep(200)

                writer.sendSSID(ssid)
                Thread.sleep(200)

                writer.sendPassword(password)
                Thread.sleep(200)

                writer.sendDeviceName(deviceName)
                Thread.sleep(200)

                writer.sendSamplingInterval(interval)
                Thread.sleep(200)

                writer.saveConfiguration()

                onCompleted?.invoke()

            } catch (e: Exception) {

                onFailed?.invoke(
                    e.message ?: "Provision failed"
                )

            }

        }.start()

    }

}