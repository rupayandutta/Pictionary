package com.example.pictionary

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes

data class Question(
    val imageDrawableRes: Int,
    val difficulty: Int = 0,
    val answer: String? = "",
    var isAsked: Boolean = false,
    var correctAnswer: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imageDrawableRes)
        parcel.writeInt(difficulty)
        parcel.writeString(answer)
        parcel.writeByte(if (isAsked) 1 else 0)
        parcel.writeByte(if (correctAnswer) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}