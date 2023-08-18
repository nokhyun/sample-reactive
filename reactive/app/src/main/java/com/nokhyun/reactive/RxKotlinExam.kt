package com.nokhyun.reactive

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable

interface IExam : LifecycleEventObserver {
    fun start()
}

class RxKotlinExam : IExam {

    private val compositeDispose = CompositeDisposable()
    private val list: List<Int> = mutableListOf<Int>().apply {
        repeat(100) {
            add(it)
        }
    }

    override fun start() {
        defaultObservable()
    }

    private fun defaultObservable() {
        list.toObservable()
            .filter { it % 2 == 0 }
            .buffer(5)
            .doOnSubscribe { logger { "RxKotlin doOnSubscribe" } }
            .subscribeBy(
                onNext = {
                    logger { "[onNext] value: $it" }
                },
                onError = {
                    logger { "[onError] value: ${it.message}" }
                },
                onComplete = {
                    logger { "[onComplete] Done!" }
                }
            ).addTo(compositeDispose)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> compositeDispose.dispose()
            else -> {
                // TODO ...
            }
        }
    }
}