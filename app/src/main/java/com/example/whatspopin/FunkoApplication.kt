package com.example.whatspopin

import android.app.Application
import com.example.whatspopin.repository.FunkoRepository

class FunkoApplication : Application() {
    val repository: FunkoRepository by lazy {FunkoRepository()}
}