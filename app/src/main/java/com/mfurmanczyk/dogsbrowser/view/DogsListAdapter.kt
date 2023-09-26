package com.mfurmanczyk.dogsbrowser.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mfurmanczyk.dogsbrowser.R
import com.mfurmanczyk.dogsbrowser.databinding.ItemDogBinding
import com.mfurmanczyk.dogsbrowser.model.DogBreed
import com.mfurmanczyk.dogsbrowser.util.getProgressDrawable

private const val TAG = "DogsListAdapter"

class DogsListAdapter (val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

    class DogViewHolder(var viewContainer: View): RecyclerView.ViewHolder(viewContainer) {

        val binding: ItemDogBinding by lazy { ItemDogBinding.bind(viewContainer) }

    }

    fun updateDogList(newDogList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.binding.dogName.text = dogsList[position].dogBreed
        holder.binding.lifespan.text = dogsList[position].lifespan
        holder.viewContainer.setOnClickListener {
            Log.i(TAG, "onBindViewHolder: navigating")
            val action = ListFragmentDirections.actionDetailFragment(dogsList[position].uuid)
            it.findNavController().navigate(action)
        }
        holder.binding.imageView.load(dogsList[position].imageUrl) {
            crossfade(true)
            placeholder(getProgressDrawable(holder.binding.imageView.context))
        }
    }

    override fun getItemCount(): Int = dogsList.size
}