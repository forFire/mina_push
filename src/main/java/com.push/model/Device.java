package com.push.model;

import java.io.Serializable;

/**
 * 车辆信息
 */
public class Device implements Serializable {

    private static final long serialVersionUID = -2639680569543322880L;

	//设备唯一编码
   private String sn;

    //车牌
	private String vehicleNo;

    //车辆颜色
   private String vehicleColor;

   //车载终端厂商唯一编码
    private String producerId;

    //车载终端型号
    private String terminalModelType;

    //车载终端编号，大写字母和数字组成
    private String terminalId;

	private String phone;


    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getTerminalModelType() {
        return terminalModelType;
    }

    public void setTerminalModelType(String terminalModelType) {
        this.terminalModelType = terminalModelType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}