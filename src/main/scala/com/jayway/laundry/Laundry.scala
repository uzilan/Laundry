package com.jayway.laundry

import java.text.SimpleDateFormat
import java.util.Calendar
import com.vaadin.ui.Alignment
import com.vaadin.ui.Window
import com.vaadin.Application

import vaadin.scala._

/**
 * A simple Vaadin application which shows the booking screen and is the main class for this application.
 */
class Laundry extends Application {

  val startTime = 7 // how early in the day can the laundry be booked
  val passLength = 3 // how long booking passes can be, in hours
  val passesPerDay = 5 // how many booking passes per day

  val cal = Calendar.getInstance()
  val sdf = new SimpleDateFormat("EE") // used for getting the name of the day

  /** Initialize the Vaadin Window and panels */
  def init(): Unit = {
    setMainWindow(new Window("SCAlable LAundry"))
    val layout = new VerticalLayout(100 pct, 100 pct)
    layout.add(createBookingPanel(createWeek()), alignment = Alignment.MIDDLE_CENTER)
    getMainWindow().setContent(layout);
  }

  /** Create a week */
  private def createWeek(): Seq[Day] = for (day <- 0 until 7) yield createDay()

  /** Create a day */
  private def createDay(): Day = new Day(for (pass <- 0 until passesPerDay) yield createPass(pass))

  /** Create a pass */
  private def createPass(pass: Int) = new Pass(startTime + passLength * pass, startTime + passLength + passLength * pass)

  /** Create the booking panel */
  private def createBookingPanel(week: Seq[Day]): Panel = {
    val panel = new Panel()
    panel.setLayout(
      new HorizontalLayout {
        add(createWeekPanel(createWeek), alignment = Alignment.MIDDLE_CENTER)
      })
    panel
  }

  /** Create a week panel */
  private def createWeekPanel(week: Seq[Day]): Panel = {
    val panel = new Panel(caption = "Week")
    panel.setLayout(
      new HorizontalLayout {
        (0 until week.size) foreach { day =>
          add(createDayPanel(week(day), day))
        }
      })
    panel
  }
  /** Create a day panel */
  private def createDayPanel(day: Day, dayNo: Int): Panel = {
    val panel = new Panel(caption = getDayName(dayNo))
    panel.setLayout(
      new VerticalLayout {
        (0 until passesPerDay) foreach { pass =>
          add(createPassPanel(day.passes(pass)))
        }
      })
    panel
  }

  /** Return a day's name given a day number (0 = Monday, 1 = Sunday etc.)  */
  private def getDayName(day: Int): String = {
    cal.set(Calendar.DAY_OF_WEEK, (day + 2) % 7)
    sdf.format(cal.getTime())
  }

  /** Create a pass panel */
  private def createPassPanel(pass: Pass): Panel = {
    val panel = new Panel()
    panel.setLayout(new HorizontalLayout {
      add(new Label(pass.toString))
    })
    panel
  }

}