package com.push.model;

import java.io.Serializable;

/*
 * 位置类
 */
public class Position implements  Serializable{
	private static final long serialVersionUID = -7222178759207809761L;
	//设备号
	private String deviceSn;
	private Double lng = 116.4985916018D;
	private Double lat = 39.9798650053D;
	/** 定位方式,A 实时*/
	private String mode;

	/** 速度 米/秒 */
	private Double speed = 0D;

	/** 方向 */
	private Float direction = 0F;


	// ************************************************

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Float getDirection() {
		return direction;
	}

	public void setDirection(Float direction) {
		this.direction = direction;
	}
}
