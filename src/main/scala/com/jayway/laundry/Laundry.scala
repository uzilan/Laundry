package com.jayway.laundry

import com.vaadin.Application
import DefaultData._
import com.vaadin.ui.{Alignment, Window, Table}
import com.vaadin.event.ItemClickEvent
import com.vaadin.ui.Table.CellStyleGenerator
import vaadin.scala._


/**
 * A simple Vaadin application which shows the booking screen and is the main class for this application.
 */
class Laundry extends Application {

  // Initialize the Vaadin Window and panels
  def init(): Unit = {

    // create the main window
    setMainWindow(new Window("SCAlable LAundry"))

    // create a header
    val header = new Label("Please choose your name and laundry booking time")

    // create a combobox with the tenants' names
    val tenantChooser = new ComboBox
    defaultTenants foreach {
      tenant => tenantChooser.addItem(tenant.name)
    }

    // create the booking table
    val bookingTable = new Table

    // add a column for each day
    defaultDays foreach {
      day =>
        bookingTable.addContainerProperty(day._2, classOf[BookingPanel], null, day._1, null, null)
    }

    // a layout containing a booking
    case class BookingPanel(booking: Booking) extends HorizontalLayout {
      add(new Label(booking.toString))

      // required to allow similar cells in the table
      override def equals(other: Any): Boolean = false

      //setStyleName("green")
    }

    // to populate the table, add a whole row (each day's bookings for one pass) at a time,
    // transfer each booking data row into booking panels array and add them as a row
    defaultWeekBookings foreach {
      bookings =>
        val bookingsPanels = bookings map (new BookingPanel(_))
        bookingTable.addItem(bookingsPanels.toArray, bookingsPanels(0).booking.pass.from)
    }

    // Send changes in selection immediately to server.
    bookingTable.setImmediate(true);

    // change the cell generator to allow different cell styles
    bookingTable.setCellStyleGenerator(new CellStyleGenerator {
      override def getStyle(itemId: AnyRef, propertyId: AnyRef): String = "cellcolored"
    });

    // listen to table clicks
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

    // add components in a panel
    val centerPanel = new VerticalLayout(600 px, 370 px) {
      Seq(header, tenantChooser, bookingTable) foreach (add(_, alignment = Alignment.MIDDLE_LEFT))
    }

    // add the panel a layout
    val windowLayout = new VerticalLayout(100 pct, 100 pct) {
      add(centerPanel, alignment = Alignment.MIDDLE_CENTER)
    }

    // add the layout in the main window
    getMainWindow.setContent(windowLayout);

    // set the theme
    setTheme("laundry")

  }


}