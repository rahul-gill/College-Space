package com.github.rahul_gill.collegespace.domain.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.github.rahul_gill.collegespace.util.Util
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.time.Duration
import java.util.*


@Keep
@Parcelize
data class Event(
    var eventName: String = "",
    var start: Date = Calendar.getInstance().time,
    var duration: DurationWrapper? = null,
    var repeatPeriod: DurationWrapper? = null,
    var description: String? = null,
    var attendanceRecord: AttendanceRecord? = null,
    var className: String? = null,
    var userGroupName: String? = null
): Parcelable{
    companion object{
        fun from(contestModel: ContestModel) = Event(
            eventName = contestModel.name,
            start = contestModel.start_time,
            duration = DurationWrapper.from(Duration.between(Util.latestLocalDateTime(contestModel.start_time), Util.latestLocalDateTime(contestModel.end_time)))
        ).also { Timber.d("before: $contestModel\n after: $it") }
    }
}

@Parcelize
data class DurationWrapper(
    var nano: Int = 0,
    var negative: Boolean = false,
    var seconds: Long = 0L
) : Parcelable {
    companion object{
        fun from(duration: Duration): DurationWrapper {
            duration.toString()
            return DurationWrapper(
                duration.nano,
                duration.isNegative,
                duration.seconds
            )
        }
    }
    fun toDuration(): Duration{
        return Duration.ZERO.withNanos(nano).withSeconds(seconds).apply { if(negative) negated()  }
    }
}