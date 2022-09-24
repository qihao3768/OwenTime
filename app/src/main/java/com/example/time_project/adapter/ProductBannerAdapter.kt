package com.example.time_project.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.time_project.R
import com.example.time_project.databinding.LayoutImageItemBinding
import com.example.time_project.databinding.LayoutVideoItemBinding
import com.example.time_project.load
import com.example.time_project.ui.DetailActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.youth.banner.adapter.BannerAdapter
import java.util.ArrayList

open class ProductBannerAdapter(
    var context: Context,
    var mData: List<String>?,
    var isDetail: Boolean = false
) :
    BannerAdapter<String, RecyclerView.ViewHolder>(mData) {

    private lateinit var exoPlayer: ExoPlayer

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            1//视频
        } else {
            2//图片
        }
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_video_item, parent, false)

            val binding = LayoutVideoItemBinding.bind(view)
            return VideoHolder(binding)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_image_item, parent, false)

            val binding = LayoutImageItemBinding.bind(view)
            return ImageHolder(binding)
        }

    }

    override fun onBindView(
        holder: RecyclerView.ViewHolder,
        data: String,
        position: Int,
        size: Int
    ) {
        if (holder is VideoHolder) {
            exoPlayer = ExoPlayer.Builder(context).build()
            exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            holder.binding.exoPlayer.player = exoPlayer
            holder.binding.exoPlayer.useController = false
            val uri = Uri.parse(data)
            val item = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(item)
            exoPlayer.prepare()

        } else if (holder is ImageHolder) {
            holder.binding.bannerImage.load(data)
        }
        if (isDetail) {
            return
        } else {
            holder.itemView.setOnClickListener {
                val intent: Intent = Intent(context, DetailActivity::class.java)
                intent.putStringArrayListExtra("777", mData as ArrayList<String>?)
                context.startActivity(intent)
            }
        }


    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    open fun stopVideo() {
        exoPlayer.pause()
    }

    open fun startVideo() {
        exoPlayer.play()
    }

    inner class VideoHolder(var binding: LayoutVideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class ImageHolder(var binding: LayoutImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}