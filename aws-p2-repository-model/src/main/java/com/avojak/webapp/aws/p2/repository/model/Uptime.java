package com.avojak.webapp.aws.p2.repository.model;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Models the application up-time.
 */
public class Uptime {

	private final long days;
	private final long hours;
	private final int minutes;

	/**
	 * Constructor.
	 *
	 * @param days
	 * 		The number of days. Must be non-negative.
	 * @param hours
	 * 		The number of hours. Must be non-negative.
	 * @param minutes
	 * 		The number of minutes. Must be non-negative.
	 */
	public Uptime(final long days, final long hours, final int minutes) {
		this.days = days;
		checkArgument(days >= 0, "days must be non-negative");
		this.hours = hours;
		checkArgument(hours >= 0, "hours must be non-negative");
		this.minutes = minutes;
		checkArgument(minutes >= 0, "minutes must be non-negative");
	}

	/**
	 * Gets the number of days.
	 *
	 * @return The non-negative number of days.
	 */
	public long getDays() {
		return days;
	}

	/**
	 * Gets the number of hours.
	 *
	 * @return The non-negative number of hours.
	 */
	public long getHours() {
		return hours;
	}

	/**
	 * Gets the number of minutes.
	 *
	 * @return The non-negative number of minutes.
	 */
	public int getMinutes() {
		return minutes;
	}

}
