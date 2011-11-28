package com.jayway.laundry

import java.awt.Color

/** A laundry pass */
case class Pass(from: Int, to: Int) {
  override def toString = from + "-" + to
}

/** A day */
case class Day(passes: Seq[Pass])

/** A tenant */
case class Tenant(name: String)

/** A booking */
case class Booking(tenant: Tenant, pass: Pass)    {
  override def toString = pass.toString
}