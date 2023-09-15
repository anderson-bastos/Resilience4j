package edu.supplier.domain.exception

class ApiErrorMessage (
    var status: String,
    var message: String? = null
)