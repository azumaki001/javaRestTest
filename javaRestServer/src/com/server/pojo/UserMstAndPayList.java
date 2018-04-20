package com.server.pojo;

import java.util.ArrayList;

import com.server.entity.PayList;
import com.server.entity.UserMst;

public class UserMstAndPayList {

	private ArrayList<UserMst> userMst;
	private ArrayList<PayList> payList;
	public ArrayList<UserMst> getUserMst() {
		return userMst;
	}
	public void setUserMst(ArrayList<UserMst> userMst) {
		this.userMst = userMst;
	}
	public ArrayList<PayList> getPayList() {
		return payList;
	}
	public void setPayList(ArrayList<PayList> payList) {
		this.payList = payList;
	}

}