package com.sandip.locus.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.viewbinding.ViewBinding
import com.sandip.locus.data.Input
import com.sandip.locus.databinding.LayoutCommentTypeBinding

class CommentView(
    private val commentBinding: LayoutCommentTypeBinding,
    private val clickCallback: ((String, Input, Int) -> Unit)?
): InputItemView<ViewBinding>(commentBinding) {


    fun bind(item: Input, hashMap: HashMap<String, String>) {
        commentBinding.title.text = item.title
        item.value = item.value ?: hashMap[item.id]
        commentBinding.switchButton.isChecked = item.isComment
        commentBinding.userComment.setText(item.value)
        commentBinding.userComment.visibility = if(item.isComment) View.VISIBLE else View.GONE
        commentBinding.switchButton.setOnCheckedChangeListener { _, b ->
            if(item.isComment != b) {
                item.isComment = b
                commentBinding.userComment.setText("")
                item.value = null
            }

            commentBinding.userComment.visibility = if(b) View.VISIBLE else View.GONE
        }

        commentBinding.userComment.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.value = s?.toString()
                s?.let {
                    hashMap[item.id] = it.toString()
                }
            }
        })
    }

}