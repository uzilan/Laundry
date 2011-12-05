package com.jayway.laundry

import java.awt.Color
import util.Random

object DefaultData {

  val startTime = 7 // how early in the day can the laundry be booked
  val passLength = 3 // how long booking passes can be, in hours
  val passesPerDay = 5 // how many booking passes per day

  lazy val rnd = new Random()

  lazy val defaultTenants = Seq("Anders Andersson", "Bertil Bertilsson", "Cesar Cesarsson", "David Davidsson", "Emil Emilsson", "Fredrik Fredriksspn") map {
    Tenant(_)
  }

  lazy val defaultDays = Seq(("Sunday", 0), ("Monday", 1), ("Tuesday", 2), ("Wednesday", 3), ("Thursday", 4), ("Friday", 5), ("Saturday", 6))

  lazy val defaultWeekBookings: Seq[Seq[Booking]] = {
    for (x <- 0 until passesPerDay) yield
      for (y <- 0 until 7) yield new Booking(null, Pass(startTime + passLength * x, startTime + passLength * (x + 1)))
  }
}