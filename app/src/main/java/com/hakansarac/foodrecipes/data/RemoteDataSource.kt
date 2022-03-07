package com.hakansarac.foodrecipes.data

import com.hakansarac.foodrecipes.data.network.FoodRecipesApi
import com.hakansarac.foodrecipes.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val foodRecipesApi: FoodRecipesApi
){

    suspend fun getRecipes(queries: Map<String,String>): Response<FoodRecipe>{
        return foodRecipesApi.getRecipes(queries)
    }
}