package com.server.entity;

import java.util.ArrayList;

public class UserAUserB_RESULT_LIST {

	private ArrayList<UserAUserB_RESULT> detailListAB;
	private ArrayList<UserAUserB_RESULT> detailListBA;

	public ArrayList<UserAUserB_RESULT> getDetailListBA() {
		return detailListBA;
	}

	public void setDetailListBA(ArrayList<UserAUserB_RESULT> detailListBA) {
		this.detailListBA = detailListBA;
	}

	public ArrayList<UserAUserB_RESULT> getDetailListAB() {
		return detailListAB;
	}

	public void setDetailListAB(ArrayList<UserAUserB_RESULT> detailListAB) {
		this.detailListAB = detailListAB;
	}
}