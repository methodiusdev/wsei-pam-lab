package pl.wsei.pam.lab07.data

import java.time.LocalDate

interface CurrentDateProvider {
    fun now(): LocalDate
}

class RealCurrentDateProvider : CurrentDateProvider {
    override fun now(): LocalDate = LocalDate.now()
}
