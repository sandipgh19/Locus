package com.sandip.locus.adapter

import android.widget.RadioButton
import androidx.viewbinding.ViewBinding
import com.sandip.locus.data.Input
import com.sandip.locus.databinding.LayoutSingleChoiseBinding


class SingleSelectionView(
    private val singleSelectionView: LayoutSingleChoiseBinding,
    private val clickCallback: ((String, Input, Int) -> Unit)?
) : InputItemView<ViewBinding>(singleSelectionView) {


    fun bind(item: Input, hashMap: HashMap<String, String>) = with(singleSelectionView) {
        singleSelectionView.title.text = item.title
        item.value = item.value ?: hashMap[item.id]
        singleSelectionView.radioGroup.removeAllViews()
        if (item.dataMap.options?.isNullOrEmpty() == false) {
            for (i in 0 until (item.dataMap.options?.size ?: 0)) {
                val button = addRadioButtons((item.dataMap.options?.get(i) ?: ""), item.value, i)
                singleSelectionView.radioGroup.addView(button)
            }
        }

        singleSelectionView.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById(i) as RadioButton?
            if(radioButton!=null) {
                radioButton.isChecked = true
                item.value = radioButton.text?.toString()
                item.value?.let {
                    hashMap[item.id] = it
                }
            }
        }
    }


    private fun addRadioButtons(text: String, value: String?, position: Int): RadioButton {
        val rdbtn = RadioButton(singleSelectionView.root.context)
        rdbtn.id = position
        rdbtn.text = text
        rdbtn.isChecked = (text == value)
        return rdbtn
    }
}