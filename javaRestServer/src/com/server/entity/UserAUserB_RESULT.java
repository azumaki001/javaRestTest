package com.server.entity;

import java.math.BigDecimal;


public class UserAUserB_RESULT {

	private BigDecimal userIdFrom;
	private BigDecimal userIdTo;
	private String userNmFrom;
	private String userNmTo;
	private BigDecimal payNum;
	private BigDecimal adNum;
	private String use;
	private BigDecimal amount;

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
	public String getUserNmFrom() {
		return userNmFrom;
	}
	public void setUserNmFrom(String userNmFrom) {
		this.userNmFrom = userNmFrom;
	}
	public String getUserNmTo() {
		return userNmTo;
	}
	public void setUserNmTo(String userNmTo) {
		this.userNmTo = userNmTo;
	}
	public BigDecimal getPayNum() {
		return payNum;
	}
	public void setPayNum(BigDecimal payNum) {
		this.payNum = payNum;
	}
	public BigDecimal getAdNum() {
		return adNum;
	}
	public void setAdNum(BigDecimal adNum) {
		this.adNum = adNum;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}