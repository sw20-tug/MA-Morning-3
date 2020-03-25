package at.tugraz.ist.sw20.mam3.cook.model.service

interface DataReadyListener<T> {
    fun onDataReady(data: T?)
}