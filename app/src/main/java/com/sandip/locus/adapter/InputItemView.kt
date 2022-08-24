package com.sandip.locus.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class InputItemView <out T : ViewBinding> constructor(val binding: T) :
RecyclerView.ViewHolder(binding.root)