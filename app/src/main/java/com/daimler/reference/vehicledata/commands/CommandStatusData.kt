package com.daimler.reference.vehicledata.commands

data class CommandStatusData(
    val error: String?,
    val state: String?,
    val processId: String?,
    val timestamp: String?
)