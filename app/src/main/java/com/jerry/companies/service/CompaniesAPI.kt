package com.jerry.companies.service

import com.jerry.companies.service.data.CompanyDto
import retrofit2.http.GET

interface CompaniesAPI {
    @GET("data.json")
    suspend fun getCompanyList(): List<CompanyDto>
}