package com.example.moengage.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.moengage.model.Articles
import com.example.moengage.R
import com.example.moengage.managers.Utils
import com.example.moengage.activity.MainActivity
import com.example.moengage.databinding.MainListBinding
import com.squareup.picasso.Picasso
import java.util.ArrayList

class MainListADTR() : RecyclerView.Adapter<MainListADTR.HomeListVH>(){

    var articleLists = ArrayList<Articles>()

    private var mOnItemClickListeners: OnItemClickListeners? = null

    interface OnItemClickListeners {
        fun onItemClick(position: Int, articleList: Articles)
        fun onViewMoreClick(position: Int, articleList: Articles)
    }

    fun setOnClickListeners(onItemClick: OnItemClickListeners) {
        this.mOnItemClickListeners = onItemClick
    }

    class HomeListVH(mBinding: MainListBinding): RecyclerView.ViewHolder(mBinding.root){
        val binding: MainListBinding = mBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListVH {
        return HomeListVH(MainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return articleLists.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HomeListVH, position: Int) {
        val articleList = articleLists[position]
        holder.binding.titleTV.text = articleList.author
        holder.binding.author.text = articleList.title
        holder.binding.authorNameTV.text = Utils.formatDateString(articleList.publishedAt ?: "")
        holder.binding.authorIV.visibility = if (articleList.author != null) VISIBLE else INVISIBLE

        if (articleList.urlToImage?.isNotEmpty() == true){
            Picasso.get()
                .load(articleList.urlToImage)
                .resize(800, 600)
                .centerInside()
                .placeholder(R.mipmap.image_placeholder)
                .into(holder.binding.articleIV)
        }

        holder.binding.viewMoreTV.setOnClickListener {
            mOnItemClickListeners?.onViewMoreClick(position, articleList)
        }

        holder.binding.parentCL.setOnClickListener {
            mOnItemClickListeners?.onItemClick(position, articleList)
        }
    }
}