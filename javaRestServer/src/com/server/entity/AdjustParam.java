package com.server.entity;

import java.math.BigDecimal;

public class AdjustParam {

	private BigDecimal userIdFrom;
	private BigDecimal userIdTo;
	private BigDecimal adAmount;
	private String adIds;
	public BigDecimal getUserIdFrom() {
		return userIdFrom;
	}
	public void setUserIdFrom(BigDecimal userIdFrom) {
		this.userIdFrom = userIdFrom;
	}
	public BigDecimal getUserIdTo() {
		return userIdTo;
	}
	public void setUserIdTo(BigDecimal userIdTo) {
		this.userIdTo = userIdTo;
	}
	public BigDecimal getAdAmount() {
		return adAmount;
	}
	public void setAdAmount(BigDecimal adAmount) {
		this.adAmount = adAmount;
	}
	public String getAdIds() {
		return adIds;
	}
	public void setAdIds(String adIds) {
		this.adIds = adIds;
	}
}