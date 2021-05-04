package com.cooking.merge.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.cooking.merge.R
import com.cooking.merge.SearchDetailsActivity
import com.cooking.merge.food_fragment.FoodDetailsf
import com.cooking.merge.models.FooditemsModel
import com.cooking.merge.models.SimpleFooditemsModel
import kotlinx.android.synthetic.main.cardview_layout.view.*
import kotlinx.android.synthetic.main.cardview_layout_search.view.*
import java.util.*
import kotlin.collections.ArrayList

class SearchitemsAdapter(private var itemList: ArrayList<FooditemsModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    //此處加上Filterable的extensions就可以自行定義filter（getFilter()）

    //var searchList = ArrayList<String>()
    var searchList = ArrayList<FooditemsModel>()

    lateinit var mcontext: Context


    init {
        searchList = itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemListView = LayoutInflater.from(parent.context).inflate(R.layout.cardview_layout, parent, false)
        val source = ListHolder(itemListView)
        mcontext = parent.context
        return source
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.icons_image.setImageResource(searchList[position].iconsChar)
        holder.itemView.title.text = searchList[position].alphaChar
        holder.itemView.title.setTextColor(Color.BLACK)

        holder.itemView.setOnClickListener {
            val intent = Intent(mcontext, FoodDetailsf::class.java)
            intent.putExtra("FOODIMAGE", searchList[position].iconsChar)
            intent.putExtra("FOODNAME", searchList[position].alphaChar)
            intent.putExtra("FOODINGREDIENTS", searchList[position].ingredient)
            intent.putExtra("FOODSAUCE", searchList[position].sauce)
            intent.putExtra("FOODLINK", searchList[position].link)


            mcontext.startActivity(intent)
            Log.d("Selected:", searchList[position].alphaChar)

        }
    }


    inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var searchIV: ImageView = itemView.icons_image
        var searchTV: TextView = itemView.title
        var searchBtn: Button = itemView.cardview_fav

    }


    override fun getFilter(): Filter {  //使用getFilter()時有兩個必須有的method: performFiltering 及 publishResults
        return object : Filter() {

            //The performFiltering method checks if we have typed a text in the SearchView.
            //If there is not any text, will return all items.
            //If there is a text, then we check if the characters match the items from the list and return the results in a FilterResults type.

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    searchList = itemList
                } else {
                    val resultList = ArrayList<FooditemsModel>()

                    val itemImageList = ArrayList<Int>()
                    val itemStringList = ArrayList<String>()
//                    val itemIngredientsList = ArrayList<ArrayList<String>>()
//                    val itemsauceList = ArrayList<ArrayList<String>>()
                    val itemIngredientsList = ArrayList<String>()
                    val itemsauceList = ArrayList<String>()
                    val itemlinkList = ArrayList<String>()
                    val itemkeyList = ArrayList<String>()
                    val itemfavList = ArrayList<String>()


                    for (i in itemList.indices){
                        itemImageList[i] = itemList[i].iconsChar
                        itemStringList[i] = itemList[i].alphaChar
                        itemIngredientsList[i] = itemList[i].ingredient
                        itemsauceList[i] = itemList[i].sauce
                        itemlinkList[i] = itemList[i].link
                        itemkeyList[i] = itemList[i].key_id
                        itemfavList[i] = itemList[i].favStatus
                    }
                    for (row in itemStringList) {
                        if (row.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            //resultList.add(row)
                            resultList.add(FooditemsModel(itemImageList[row.lastIndex],itemStringList[row.lastIndex],
                                itemIngredientsList[row.lastIndex],itemsauceList[row.lastIndex],
                                itemlinkList[row.lastIndex],itemkeyList[row.lastIndex],itemfavList[row.lastIndex]))
                        }
                    }
                    searchList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = searchList
                return filterResults
            }

            //The publishResults get these results, passes it to the countryFilterList array and updates the RecyclerView.
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchList = results?.values as ArrayList<FooditemsModel>
                notifyDataSetChanged()
            }

        }
    }

}