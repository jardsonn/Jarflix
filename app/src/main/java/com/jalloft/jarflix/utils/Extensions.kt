package com.jalloft.jarflix.utils


val Int.toHour get() = div(60).mod(24).toString().plus("h : ").plus(mod(60)).plus("min")