package com.prudent.fms.ui.base

/**
 * Created by Dharmik Patel on 20-Jul-17.
 */
interface Presenter<in T : View> {
    fun onAttach(view: T)

    fun onDetach()
}