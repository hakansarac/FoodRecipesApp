package com.hakansarac.foodrecipes.views.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.hakansarac.foodrecipes.MainViewModel
import com.hakansarac.foodrecipes.R
import com.hakansarac.foodrecipes.adapters.RecipesAdapter
import com.hakansarac.foodrecipes.util.Constants.Companion.API_KEY
import com.hakansarac.foodrecipes.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mView : View
    private lateinit var shimmerRecyclerView : ShimmerRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        shimmerRecyclerView = mView.findViewById(R.id.shimmer_recycler_view)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecyclerView()
        requestApiData()

        return mView
    }

    private fun requestApiData(){
        mainViewModel.getRecipes(applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner,{ response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let{
                        mAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()

                }
            }
        })
    }

    private fun applyQueries(): HashMap<String,String>{
        val queries : HashMap<String,String> = HashMap()

        /**
         * https://api.spoonacular.com/recipes/complexSearch?number={number}&apiKey={api}&type={type}...
         */
        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"

        return queries
    }

    private fun setupRecyclerView(){
        shimmerRecyclerView.adapter = mAdapter
        shimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect(){
        shimmerRecyclerView.showShimmerAdapter()
    }

    private fun hideShimmerEffect(){
        shimmerRecyclerView.hideShimmerAdapter()
    }
}