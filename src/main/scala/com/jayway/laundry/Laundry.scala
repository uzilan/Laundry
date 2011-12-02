package com.jayway.laundry

import java.util.Calendar
import com.vaadin.Application
import DefaultData._
import com.vaadin.ui.{Alignment, Window, Table}
import vaadin.scala._
import com.vaadin.event.ItemClickEvent
import com.vaadin.ui.Table.CellStyleGenerator


/**
 * A simple Vaadin application which shows the booking screen and is the main class for this application.
 */
class Laundry extends Application {


  val cal = Calendar.getInstance()

  /**Initialize the Vaadin Window and panels */
  def init(): Unit = {
    setMainWindow(new Window("SCAlable LAundry"))

    val header = new Label("Please choose your name and laundry booking time")

    val tenantChooser = new ComboBox()
    defaultTenants foreach {
      tenant => tenantChooser.addItem(tenant.name)
    }

    // create the booking table add a column for each day
    val bookingTable = new Table()
    defaultDays foreach {
      day =>
        bookingTable.addContainerProperty(day._2, classOf[BookingPanel], null, day._1, null, null)
    }

    // a layout containing a booking
    case class BookingPanel(booking: Booking) extends HorizontalLayout {
      add(new Label(booking.toString))
      //setStyleName("green")
    }


    // to populate the table, add a whole row (each day's bookings for one pass) at a time
    // transfer each booking data row into booking panels and add them as a row
    defaultWeekBookings foreach {
      bookings =>
      //val bookingsPanels = bookings map (booking => new BookingPanel(booking))
      //bookingTable.addItem(bookingsPanels.toArray, bookingsPanels(0).booking.pass.from)
        bookingTable.addItem(Seq("1", "2", "3", "4", "5", "6", "7"))
    }

    // Send changes in selection immediately to server.
    bookingTable.setImmediate(true);

    bookingTable.setCellStyleGenerator(new CellStyleGenerator {
      override def getStyle(itemId: AnyRef, propertyId: AnyRef): String = "cellcolored"
    });


    bookingTable.addListener(new ItemClickEvent.ItemClickListener {

      def itemClick(event: ItemClickEvent) {
        val item = event.getItem
        val itemId = event.getItemId
        val propertyId = event.getPropertyId

        println(String.format("item: %s, itemId: %s, propertyId: %s", item, itemId, propertyId))

        val bookingPanels = item.asInstanceOf[Seq[BookingPanel]]
        bookingPanels(event.getPropertyId.asInstanceOf[Int]).setStyleName("green")
      }
    })

    val centerPanel = new VerticalLayout(600 px, 370 px) {
      Seq(header, tenantChooser, bookingTable) foreach (add(_, alignment = Alignment.MIDDLE_LEFT))
    }

    val windowLayout = new VerticalLayout(100 pct, 100 pct) {
      add(centerPanel, alignment = Alignment.MIDDLE_CENTER)
    }

    getMainWindow.setContent(windowLayout);

    setTheme("laundry")
  }


}