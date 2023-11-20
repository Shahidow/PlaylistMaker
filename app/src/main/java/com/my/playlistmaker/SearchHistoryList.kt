package com.my.playlistmaker

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class SearchHistoryList {
    private val list = mutableListOf<Track>()
    private val subject = PublishSubject.create<List<Track>>()

    fun addList(trackList: ArrayList<Track>) {
        list.clear()
        list.addAll(trackList)
    }

    fun addTrack(item: Track) {
        if (list.isNotEmpty()) {
            if (list.contains(item)) {
                list.remove(item)
            }
            if (list.size > 9) {
                list.removeLast()
            }
        }
        list.add(0, item)
        subject.onNext(list)
    }

    fun clear() {
        list.clear()
        subject.onNext(list)
    }

    fun get(): List<Track> {
        return list
    }

    fun listen(): Observable<List<Track>> = subject
}
