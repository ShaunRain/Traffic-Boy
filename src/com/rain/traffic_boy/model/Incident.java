package com.rain.traffic_boy.model;

import java.io.Serializable;

public class Incident implements Serializable {

	private static final long serialVersionUID = -1079939120109431991L;

	private double laltitude;
	private double longtitude;
	private String type;
	private String detail;
	private String image;
	private String time;
	private String addr;

	public Incident(String type, double la, double lo, String addr,
			String time, String detail, String image) {
		this.laltitude = la;
		this.longtitude = lo;
		this.addr = addr;
		this.type = type;
		this.detail = detail;
		this.image = image;
		this.time = time;
	}

	public double getLaltitude() {
		return laltitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public String getAddr() {
		return addr;
	}

	public String getType() {
		return type;
	}

	public String getDetail() {
		return detail;
	}

	public String getImage() {
		return image;
	}

	public String getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "type:" + type + "\n" + "la:" + laltitude + "\n" + "lo:"
				+ longtitude + "\n" + "time:" + time + "\n" + "detail" + detail
				+ "\n" + "image:" + image + "\n";
	}

}
