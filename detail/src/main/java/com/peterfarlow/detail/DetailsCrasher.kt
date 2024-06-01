package com.peterfarlow.detail

class DetailsCrasher {
    fun crash(): Nothing = throw RuntimeException("crashed from details!")
}
