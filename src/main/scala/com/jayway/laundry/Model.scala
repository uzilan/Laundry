package com.jayway.laundry

/** A laundry pass */
case class Pass(from: Int, to: Int) {
  override def toString = from + "-" + to
}

/** A day */
case class Day(passes: Seq[Pass])
