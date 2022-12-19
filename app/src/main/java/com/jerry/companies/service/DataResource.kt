package com.jerry.companies.service

data class DataResource<out DataType>(
    val status: Status,
    val data: DataType? = null,
    val throwable: Throwable? = null) {

    val isIdle get() = status == Status.IDLE
    val isSuccessful get() = status == Status.DONE
    val isLoading get() = status == Status.LOADING
    val isError get() = status == Status.ERROR

    companion object {

        fun <DataType> idle() = DataResource<DataType>(Status.IDLE)

        fun <DataType> done(data: DataType) = DataResource(Status.DONE, data)

        fun <DataType> loading(data: DataType? = null) = DataResource(Status.LOADING, data)

        fun <DataType> error(throwable: Throwable?, data: DataType? = null) =
            DataResource(Status.ERROR, data, throwable)
    }
}