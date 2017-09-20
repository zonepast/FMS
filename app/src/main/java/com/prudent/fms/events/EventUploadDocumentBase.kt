package com.prudent.fms.events

import com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Response.TranscationUploadDocumentResponse

/**
 * Created by Dharmik Patel on 16-Aug-17.
 */
open class EventUploadDocumentBase() {

    lateinit var response: TranscationUploadDocumentResponse

    constructor(response: TranscationUploadDocumentResponse) : this() {
        this.response = response
    }

}