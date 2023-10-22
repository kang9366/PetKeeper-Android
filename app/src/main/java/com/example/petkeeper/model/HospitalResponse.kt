package com.example.petkeeper.model

data class HospitalInfo(
    val HOSPITAL_NAME: String,
    val HOSPITAL_PHONE: String,
    val HOSPITAL_ADDRESS: String,
    val HOSPITAL_X: Double,
    val HOSPITAL_Y: Double
)

data class HospitalResponse(
    val data: List<HospitalInfo>
)