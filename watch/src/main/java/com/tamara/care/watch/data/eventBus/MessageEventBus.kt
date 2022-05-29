package com.tamara.care.watch.data.eventBus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


object MessageEventBus {

    private var bus = PublishSubject.create<EventModel>()

    fun send(event: EventModel) {
        bus.onNext(event)
    }

    fun toObservable(): Observable<EventModel> {
        return bus
    }

}