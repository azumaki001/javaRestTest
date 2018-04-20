package com.server.entity;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PayList {

	private BigDecimal payId;
	private BigDecimal payAmount;
	private String payer;
	private String payForusers;
	private String payContent;
	private String registDate;
	private String adDone;

	private ArrayList<Detail> details;

	public BigDecimal getPayId() {
		return payId;
	}

	public void setPayId(BigDecimal payId) {
		this.payId = payId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getPayForusers() {
		return payForusers;
	}

	public void setPayForusers(String payForusers) {
		this.payForusers = payForusers;
	}

	public String getPayContent() {
		return payContent;
	}

	public void setPayContent(String payContent) {
		this.payContent = payContent;
	}

	public String getRegistDate() {
		return registDate;
	}

	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}

	public String getAdDone() {
		return adDone;
	}

	public void setAdDone(String adDone) {
		this.adDone = adDone;
	}

	public ArrayList<Detail> getDetails() {
		return details;
	}

	public void setDetails(ArrayList<Detail> details) {
		this.details = details;
	}

}