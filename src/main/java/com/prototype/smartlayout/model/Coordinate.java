package com.prototype.smartlayout.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class Coordinate implements Comparable<Coordinate> {
	private double x;
	private double y;

	@Override
	public int compareTo (@NotNull Coordinate o) {
		int result = Double.compare(this.getX(), o.getX());
		if ( result == 0 ) {
			// both X are equal -> compare Y too
			result = Double.compare(this.getY(), o.getY());
		}
		return result;
	}
}
