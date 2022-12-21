package com.loki.ripoti.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserReports(
 val name: String,
 val report: Reports
): Parcelable
