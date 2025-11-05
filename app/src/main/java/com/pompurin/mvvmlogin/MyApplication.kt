package com.pompurin.mvvmlogin

import android.app.Application
import org.maplibre.android.MapLibre

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Esta es la línea que soluciona el crash
        // Inicializa la librería MapLibre para toda la app
        // Usará "this" como el Contexto
        MapLibre.getInstance(this)

        // Nota: No necesitas pasar una API key aquí.
        // La librería es lo suficientemente inteligente para leer
        // el "meta-data" de tu AndroidManifest si un estilo
        // futuro lo llegara a necesitar.
    }
}