package com.my.playlistmaker.domain.sharing.impl

import com.my.playlistmaker.data.sharing.SharingRepository
import com.my.playlistmaker.domain.sharing.SharingInteractor

class SharingInteractorImpl(private val sharingRepository: SharingRepository) :
    SharingInteractor {

    override fun share() {
        sharingRepository.share()
    }

    override fun support() {
        sharingRepository.support()
    }

    override fun terms() {
        sharingRepository.terms()
    }
}