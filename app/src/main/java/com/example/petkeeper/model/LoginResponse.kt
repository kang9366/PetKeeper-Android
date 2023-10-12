package com.example.petkeeper.model

data class LoginResponse(
    val token: String,
    val USER: UserResponse
)

data class UserResponse(
    val USER_ID: Int,
    val USER_OAUTH_ID: Any?,
    val USER_EMAIL: String,
    val USER_NAME: String,
    val USER_PHONE: String,
    val USER_BIRTHDATE: Any?,
    val USER_IMAGE: Any?,
    val USER_AUTH: String,
    val USER_DATE: String,
    val USER_TIME: Any?,
    val USER_OAUTH_PROVIDER: Any?,
    val USER_EMAIL_VERIFIED: Any?,
    val p_pets: List<Pet>
)

data class Pet(
    val PET_ID: Int,
    val USER_ID: Int,
    val PET_NAME: String,
    val PET_KIND: String,
    val PET_SPECIES: Any?,
    val PET_GENDER: String,
    val PET_BIRTHDATE: String,
    val PET_IMAGE: String,
    val PET_DATE: String,
    val PET_TIME: String,
    val p_pet_vaccinations: List<PetVaccination>,
    val p_pet_weights: List<PetWeight>
)

data class PetVaccination(
    val PET_VACCINATION_ID: Int,
    val PET_ID: Int,
    val PET_VACCINATION_NAME: String,
    val PET_VACCINATION_PERIOD: Int,
    val PET_VACCINATION_DATE: String,
    val PET_VACCINATION_TIME: Any?
)

data class PetWeight(
    val PET_WEIGHT_ID: Int,
    val PET_ID: Int,
    val PET_WEIGHT: Double,
    val PET_WEIGHT_DATE: String,
    val PET_WEIGHT_TIME: Any?
)