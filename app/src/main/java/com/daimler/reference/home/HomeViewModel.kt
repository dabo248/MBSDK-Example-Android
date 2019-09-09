package com.daimler.reference.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.daimler.mbloggerkit.MBLoggerKit
import com.daimler.mbingresskit.MBIngressKit
import com.daimler.mbcarkit.MBCarKit
import com.daimler.mbcarkit.business.model.command.*
import com.daimler.mbcarkit.business.model.vehicle.Tank
import com.daimler.mbcarkit.business.model.vehicle.VehicleStatus
import com.daimler.mbcarkit.socket.observable.VehicleObserver
import com.daimler.mbnetworkkit.socket.ConnectionState
import com.daimler.mbnetworkkit.socket.SocketConnectionListener
import com.daimler.mbnetworkkit.socket.message.Observables
import com.daimler.mbnetworkkit.socket.message.dispose
import com.daimler.mbnetworkkit.socket.message.observe
import com.daimler.mbuikit.lifecycle.events.MutableLiveEvent
import com.daimler.mbuikit.utils.extensions.getString
import com.daimler.reference.R
import com.daimler.reference.vehicledata.commands.CommandStatusData

class HomeViewModel(app: Application) : AndroidViewModel(app), SocketConnectionListener {

    val vehicleAvailable = MutableLiveData<Boolean>()
    val vehicleStatus = MutableLiveData<VehicleStatus>()
    val tank = MutableLiveData<Tank>()

    val doorCommandProcessing = MutableLiveData<Boolean>(false)
    val doorCommandData = MutableLiveData<CommandStatusData>()

    val commandsEnabled = MutableLiveData<Boolean>(false)

    val commandSendError = MutableLiveEvent<String>()

    private var observables: Observables? = null

    private val vehicleStatusObserver = VehicleObserver.VehicleStatus { onVehicleStatusUpdate(it) }
    private val tankObserver = VehicleObserver.Tank { onTankChanged(it) }

    init {
        onVehicleChange(MBCarKit.selectedFinOrVin())
        connectToWebSocket()
    }

    override fun onCleared() {
        super.onCleared()
        clearObservers()
        MBCarKit.unregisterFromSocket(this)
    }

    fun onLockDoorsClicked() {
        MBCarKit.selectedFinOrVin()?.let { finOrVin ->
            MBCarKit.sendVehicleCommand(VehicleCommand.DoorsLock(finOrVin),object : VehicleCommandCallback<DoorsLockError> {
                override fun onError(timestamp: Long?, errors: List<DoorsLockError>) {
                    onCommandError(errors, timestamp)
                }

                override fun onSuccess(timestamp: Long?) {
                    onCommandSuccess(timestamp)
                }

                override fun onUpdate(status: VehicleCommandStatusUpdate) {
                    onCommandUdate(status)
                }
            } )
        } ?: MBLoggerKit.e("No vehicle selected.")
    }

    override fun connectionStateChanged(connectionState: ConnectionState) {
        when (connectionState) {
            ConnectionState.Closed ->
                MBLoggerKit.i("Socket connection is closed.")
            ConnectionState.Disconnected -> {
                MBLoggerKit.i("Socket is disconnected.")
                clearObservers()
                disableCommands()
            }
            ConnectionState.Connecting ->
                MBLoggerKit.i("Socket is connecting.")
            is ConnectionState.Connected -> {
                MBLoggerKit.i("Socket is connected.")
                observables = connectionState.observables.apply {
                    registerSocketObservers(this)
                }
            }
            is ConnectionState.ConnectionLost -> {
                MBLoggerKit.w("Socket connection lost.")
            }
        }
    }

    fun vehicleSelected(finOrVin: String) {
        onVehicleChange(finOrVin)
    }

    fun vehicleDeleted(finOrVin: String) {
        if (MBCarKit.selectedFinOrVin() == null) {
            onVehicleChange(null)
        }
    }

    private fun onVehicleChange(finOrVin: String?) {
        finOrVin?.let {
            showCurrentVehicleState(it)
            vehicleAvailable.postValue(true)
            clearDoorCommand()
            enableCommands()
        } ?: run {
            clearContent()
            disableCommands()
        }
    }

    private fun onCommandUdate(status: VehicleCommandStatusUpdate) {
        doorCommandData.postValue(
            CommandStatusData(
                null,
                status.status.name,
                null,
                status.updatedTimestamp?.toString()
            )
        )
    }

    private fun onCommandError(errors: List<DoorsLockError>, timestamp: Long?) {
        clearDoorCommand()
        doorCommandData.postValue(
            CommandStatusData(
                errors.joinToString { it.errorString() },
                VehicleCommandStatus.FAILED.name,
                null,
                timestamp?.toString()
            )
        )
    }

    private fun onCommandSuccess(timestamp: Long?) {
        doorCommandProcessing.postValue(false)
        doorCommandData.postValue(
            CommandStatusData(
                null,
                VehicleCommandStatus.FINISHED.name,
                null,
                timestamp?.toString()
            )
        )
    }

    private fun showCurrentVehicleState(finOrVin: String) {
        val state = MBCarKit.loadCurrentVehicleState(finOrVin)
        onVehicleStatusUpdate(state)
        onTankChanged(state.tank)
    }

    private fun enableCommands() {
        commandsEnabled.postValue(true)
    }

    private fun disableCommands() {
        commandsEnabled.postValue(false)
    }

    private fun clearContent() {
        vehicleAvailable.postValue(false)
        vehicleStatus.postValue(null)
        tank.postValue(null)
        clearDoorCommand()
    }

    private fun clearDoorCommand() {
        doorCommandProcessing.postValue(false)
        doorCommandData.postValue(null)
    }

    private fun connectToWebSocket() {
        MBIngressKit.refreshTokenIfRequired()
            .onComplete { token ->
                MBCarKit.connectToWebSocket(token.jwtToken.plainToken, this)
            }.onFailure {
                MBLoggerKit.e("Failed to refresh token.", throwable = it)
            }
    }

    private fun clearObservers() {
        observables?.let { unregisterSocketObservers(it) }
        observables = null
    }

    private fun registerSocketObservers(observables: Observables) {
        observables.observe(vehicleStatusObserver)
        observables.observe(tankObserver)
    }

    private fun unregisterSocketObservers(observables: Observables) {
        observables.dispose(vehicleStatusObserver)
        observables.dispose(tankObserver)
    }

    private fun onVehicleStatusUpdate(vehicleStatus: VehicleStatus) {
        MBLoggerKit.d("Received new vehicle status for ${vehicleStatus.finOrVin}.")
        this.vehicleStatus.postValue(vehicleStatus)
    }

    private fun onTankChanged(tank: Tank) {
        MBLoggerKit.d("Received new tank values.")
        this.tank.postValue(tank)
    }

    private fun DoorsLockError.errorString(): String {
        return when (this) {
            is DoorsLockError.GenericError -> genericError::class.java.simpleName
            else -> {
                this.error?.let {
                    it::class.java.simpleName
                }?:getString(R.string.command_error_unknown)
            }
        }
    }
}