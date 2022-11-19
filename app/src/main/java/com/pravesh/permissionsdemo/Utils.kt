package com.pravesh.permissionsdemo

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.permanentlyDenied() : Boolean{
    return !hasPermission && !shouldShowRationale
}