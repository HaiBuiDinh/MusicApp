package com.aemyfiles.musicapp.External.ui.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aemyfiles.musicapp.Domain.entity.ItemType
import com.aemyfiles.musicapp.External.services.MediaPlayService
import com.aemyfiles.musicapp.External.thumbnail.ThumbnailManager
import com.aemyfiles.musicapp.External.ui.activities.MainActivity
import com.aemyfiles.musicapp.External.ui.adapter.ShowListSongAdapter
import com.aemyfiles.musicapp.External.utils.BItmapUtils
import com.aemyfiles.musicapp.Presenter.controller.DetailController
import com.aemyfiles.musicapp.Presenter.dagger2.DaggerAppComponent
import com.aemyfiles.musicapp.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_detail_layout.*


class DetailFragment(val mService: MediaPlayService) : AbsFragment<DetailController>() {

    companion object{
        const val ID = "id"
        const val TYPE = "type"
        const val TITLE = "title"
        const val THUMBNAIL = "thumbnail"
    }

    lateinit var mAdapter:ShowListSongAdapter

    override fun initController() {
        val component = DaggerAppComponent.builder().application(activity!!.application).activity(activity!!).build()
        component.inject(this)
    }

    override val resourceId: Int
        get() = R.layout.fragment_detail_layout

    override fun updateContentView(isServiceConnected: Boolean) {
        TODO("Not yet implemented")
    }

    @SuppressLint("ResourceAsColor")
    override fun initUI(view: View?) {

        val bundle = arguments;
        val id = bundle!!.getInt(ID)
        val type = bundle.getSerializable(TYPE) as ItemType
        val title = bundle.getString(TITLE)
        val thumbnail = bundle.getString(THUMBNAIL)
        collapsing_toolbar_image_view_real.clipToOutline = true

        if(!thumbnail.equals(ThumbnailManager.NOTHUMBNAIL)){
            ThumbnailManager.getInstance().loadThumbnail(ThumbnailManager.ThumbnailInfo(collapsing_toolbar_image_view_real, id, thumbnail, type, object : ThumbnailManager.ThumbnailCallback{
                override fun onSuccess(albumId: Int, path: String?, bitmap: Bitmap?) {
                    collapsing_toolbar_image_view.setImageBitmap(BItmapUtils.blurRenderScript(activity, bitmap!!, 15))
                }
            }))
        } else{
            val res = ItemType.getIdByItemType(type)
            collapsing_toolbar_image_view_real.setImageResource(res)
            val bitmap = resources.getDrawable(res).toBitmap()
            collapsing_toolbar_image_view.setImageBitmap(BItmapUtils.blurRenderScript(activity, bitmap!!, 15))
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbar_detail)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_detail.setNavigationOnClickListener({
            activity?.onBackPressed()
        })
        collapsing_toolbar_appbarlayout.setExpanded(true)
        collapsing_toolbar_layout.title = title

        collapsing_toolbar_appbarlayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

            }
        })
        collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf((resources.getColor(android.R.color.black))))
        collapsing_toolbar_layout.setCollapsedTitleTextColor(resources.getColor(android.R.color.black))

        mAdapter = ShowListSongAdapter(mService)
        rc_list_detail.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }

        mController.getListItems().observe(activity!!, Observer {
            val isEmpty = it == null || it.isEmpty()
            if(isEmpty){
                empty_view_detail?.visibility = View.VISIBLE
                rc_list_detail?.visibility = View.GONE
            } else{
                empty_view_detail?.visibility = View.GONE
                rc_list_detail?.visibility = View.VISIBLE
                mAdapter.setData(it)
            }
        })

        mController.getListSongById(id, type)
        registerReceiver()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("hai.bui1", "onReceive: all song ")
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(MainActivity.UPDATE_LAYOUT)
        activity!!.applicationContext.registerReceiver(broadcastReceiver, intentFilter)
    }
}