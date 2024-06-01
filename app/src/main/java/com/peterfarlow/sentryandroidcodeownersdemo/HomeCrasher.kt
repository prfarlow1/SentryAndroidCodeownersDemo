package com.peterfarlow.sentryandroidcodeownersdemo

class HomeCrasher {
    fun crash(): Nothing = throw RuntimeException("crashed from home button!")
}
