package com.pravesh.permissionsdemo

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.pravesh.permissionsdemo.ui.theme.PermissionsDemoTheme

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsDemoTheme {
                val permissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                    )
                )

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecycleOwner){
                    val observer = LifecycleEventObserver{_,event->
                        if(event == Lifecycle.Event.ON_START){
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    permissionState.permissions.forEach { perm ->
                        when(perm.permission){
                            Manifest.permission.CAMERA -> {
                                when{
                                    perm.hasPermission -> Text(text = "Camera Permission accepted")
                                    perm.shouldShowRationale -> Text(text = "Camera permission is required to work properly")
                                    perm.permanentlyDenied() -> Text(text = "Please grant camera permission from app settings")
                                }
                            }
                            Manifest.permission.RECORD_AUDIO ->{
                                when{
                                    perm.hasPermission -> Text(text = "Record Audio Permission accepted")
                                    perm.shouldShowRationale -> Text(text = "Record Audio is required to work properly")
                                    perm.permanentlyDenied() -> Text(text = "Please grant Record Audio permission from app settings")
                                }
                            }

                        }
                    }
                }



            }
        }
    }
}