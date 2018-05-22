package com.gcreative_apps_studio.itemtouch

interface FavoriteItemTouchAdapter {
    fun onItemDismiss(position: Int?)
    fun onItemMove(fromPosition: Int?, toPosition: Int?): Boolean
}