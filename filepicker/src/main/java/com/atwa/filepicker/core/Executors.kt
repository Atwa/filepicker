package com.atwa.filepicker.core

import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Executors {
    val executor: Executor by lazy { Executors.newFixedThreadPool(4) }
}