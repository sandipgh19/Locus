package com.sandip.locus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sandip.locus.adapter.CommentView
import com.sandip.locus.adapter.PhotoItemView
import com.sandip.locus.adapter.SingleSelectionView
import com.sandip.locus.data.Input
import com.sandip.locus.databinding.LayoutCommentTypeBinding
import com.sandip.locus.databinding.LayoutPhotoTypeBinding
import com.sandip.locus.databinding.LayoutSingleChoiseBinding
import java.util.zip.Inflater

class InputListAdapter(
    appExecutors: AppExecutors,
    private val clickCallback: ((String, Input, Int) -> Unit)?
) : ListAdapter<Input, RecyclerView.ViewHolder>(
    AsyncDifferConfig.Builder(InputDiffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).type) {
            PHOTO_TYPE -> PHOTO_TYPE_ITEM
            SINGLE_CHOICE_TYPE -> SINGLE_CHOICE_TYPE_ITEM
            else -> COMMENT_TYPE_ITEM
        }
    }

    private val hashMap: HashMap<String, String> = HashMap()

    object InputDiffCallback : DiffUtil.ItemCallback<Input>() {
        override fun areItemsTheSame(oldItem: Input, newItem: Input): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Input, newItem: Input): Boolean {
            return oldItem.title == newItem.title && oldItem.type == newItem.type && oldItem.value == newItem.value
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            PHOTO_TYPE_ITEM -> PhotoItemView(photoItemView = getImageView(parent), clickCallback = clickCallback) //
            SINGLE_CHOICE_TYPE_ITEM -> SingleSelectionView(singleSelectionView = getSingleSelectionView(parent), clickCallback = clickCallback)
            else -> CommentView(commentBinding = getCommentView(parent), clickCallback = clickCallback)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PhotoItemView -> holder.bind(getItem(position), hashMap, position)
            is SingleSelectionView-> holder.bind(getItem(position), hashMap)
            is CommentView -> holder.bind(getItem(position), hashMap)
        }
    }

    private fun getImageView(parent: ViewGroup): LayoutPhotoTypeBinding {
        return LayoutPhotoTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    private fun getSingleSelectionView(parent: ViewGroup): LayoutSingleChoiseBinding {
        return LayoutSingleChoiseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    private fun getCommentView(parent: ViewGroup): LayoutCommentTypeBinding {
        return LayoutCommentTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }
}