package com.example.moengage.bottomSheetDialog

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.moengage.R
import com.example.moengage.databinding.FragmentViewAllDetailsBsdBinding
import com.example.moengage.managers.Utils
import com.example.moengage.model.Articles
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class ViewAllDetailsBSD(private var articleList: Articles) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentViewAllDetailsBsdBinding
    private lateinit var mContext: Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentViewAllDetailsBsdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        setupData()
    }

    /**
     * Setup data for the UI elements
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupData(){
        if (articleList.urlToImage?.isNotEmpty() == true){
            Picasso.get()
                .load(articleList.urlToImage)
                .resize(800, 600)
                .centerInside()
                .placeholder(R.mipmap.image_placeholder)
                .into(binding.articleIV)
        }
        binding.authorNameTV.text = articleList.author
        binding.titleTV.text = articleList.title
        binding.descriptionTV.text = articleList.description

        binding.publishedOnTV.text = Utils.formatDateString(articleList.publishedAt?: "")
        if (articleList.content != null){
            binding.contentTV.text = articleList.content
        } else {
            binding.content.visibility = GONE
            binding.contentTV.visibility = GONE
            binding.view1.visibility = GONE
        }
    }

}