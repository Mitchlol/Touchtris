package com.mitchelllustig.touchtris.data

enum class Positions {
    L5, L4, L3, L2, L1, R1, R2, R3, R4, R5;

    companion object {
        val STARTING_POSITION: Positions = R1
    }
}