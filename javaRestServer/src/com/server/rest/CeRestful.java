package com.server.rest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.server.entity.AdjustParam;
import com.server.entity.Detail;
import com.server.entity.HelloResult;
import com.server.entity.PayList;
import com.server.entity.UserAUserB_RESULT;
import com.server.entity.UserAUserB_RESULT_LIST;
import com.server.entity.UserAUserB_SEL_KEY;
import com.server.entity.UserList;
import com.server.entity.UserMst;
import com.server.pojo.UserMstAndPayList;


@Path("MindsCustomerInfoReception/")
public class CeRestful {

	private static Connection conn = null;

	private static ArrayList <UserMst> userList = null;
	private static HashMap userMap = null;

	private static Connection getConn () throws SQLException, ClassNotFoundException {
		conn = null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@10.33.203.6:1524:pxs18", "ASY_WK_268",
				"ASY_WK_268");
		return conn;
	}

	private static void initConn() throws SQLException, ClassNotFoundException {

		conn = getConn();

		PreparedStatement stmt = conn.prepareStatement("select * from user_mst");
		ResultSet rset = stmt.executeQuery();
		userList = new ArrayList<UserMst>();
		userMap = new HashMap();
		while (rset.next()) {
			UserMst um = new UserMst();
			um.setId(rset.getBigDecimal("ID"));
			um.setName(rset.getString("NAME"));
			userList.add(um);
			userMap.put(rset.getBigDecimal("ID").toString(),
					rset.getString("NAME"));
		}
	}

	private static final String SELECT_SIHARAI;
	static {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT");
		sb.append("  B.PAY_ID ");
		sb.append("  , B.PAY_AMOUNT ");
		sb.append("  , B.PAY_PAYER ");
		sb.append("  , B.PAY_FOR_USERS ");
		sb.append("  , B.PAY_CONTENT ");
		sb.append("  , B.REGIST_DATE ");
		sb.append("  , LISTAGG(D.AD_FROM,',') WITHIN GROUP(order by D.AD_DETAIL_ID nulls last) AD_FROM ");
		sb.append("  , LISTAGG(D.AD_DONE,',') WITHIN GROUP(order by D.AD_DETAIL_ID nulls last) AD_DONE ");
		sb.append("  , LISTAGG(D.AD_AMOUNT,',') WITHIN GROUP(order by D.AD_DETAIL_ID nulls last) AD_AMOUNT ");
		sb.append("  , LISTAGG(D.AD_DATE,',') WITHIN GROUP(order by D.AD_DETAIL_ID nulls last) AD_DATE ");
		sb.append("FROM ");
		sb.append("  ASY_WK_268.PAY_BIZ B ");
		sb.append("LEFT OUTER JOIN ");
		sb.append("  ASY_WK_268.ADJUSTMENT_DETAIL D ");
		sb.append("ON ");
		sb.append("  B.PAY_ID = D.PAY_ID ");
		sb.append("GROUP BY ");
		sb.append("  B.PAY_ID ");
		sb.append("  , B.PAY_AMOUNT ");
		sb.append("  , B.PAY_PAYER ");
		sb.append("  , B.PAY_FOR_USERS ");
		sb.append("  , B.PAY_CONTENT ");
		sb.append("  , B.REGIST_DATE ");
		sb.append("ORDER BY ");
		sb.append("  B.REGIST_DATE DESC");

		SELECT_SIHARAI = sb.toString();
	}

	private static final String INSERT_PAY_BIZ;
	static {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append("  ASY_WK_268.PAY_BIZ ( ");
		sb.append("  PAY_ID, ");
		sb.append("  PAY_PAYER, ");
		sb.append("  PAY_FOR_USERS, ");
		sb.append("  PAY_CONTENT, ");
		sb.append("  PAY_AMOUNT, ");
		sb.append("  REGIST_DATE) ");
		sb.append("VALUES ( ");
		sb.append("  ?, ");
		sb.append("  ?, ");
		sb.append("  ?, ");
		sb.append("  ?, ");
		sb.append("  ?, ");
		sb.append("  SYSDATE) ");
		INSERT_PAY_BIZ = sb.toString();
	}

	private static final String INSERT_AD_DETAIL;
	static {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append("  ASY_WK_268.ADJUSTMENT_DETAIL ( ");
		sb.append("  AD_DETAIL_ID, ");
		sb.append("  PAY_ID, ");
		sb.append("  AD_AMOUNT, ");
		sb.append("  AD_FROM, ");
		sb.append("  AD_TO, ");
		sb.append("  AD_DATE, ");
		sb.append("  AD_DONE) ");
		sb.append("VALUES ( ");
		sb.append("  ?, ");
		sb.append("  ?, ");
		sb.append("  ?, ");
		sb.append("  ?, ");
		sb.append("  ?, ");
		sb.append("  SYSDATE, ");
		sb.append("  ?) ");
		INSERT_AD_DETAIL = sb.toString();
	}

	private static final String SELECT_AtoB;
	static {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("  A.AD_DETAIL_ID ");
		sb.append("  ,A.PAY_ID ");
		sb.append("  ,A.AD_AMOUNT ");
		sb.append("  ,UA.NAME NAMEA ");
		sb.append("  ,UB.NAME NAMEB ");
		sb.append("  ,A.AD_DATE ");
		sb.append("  ,A.AD_DONE ");
		sb.append("  ,P.PAY_CONTENT ");
		sb.append("  ,A.AD_FROM ");
		sb.append("  ,A.AD_TO ");
		sb.append("FROM ");
		sb.append("  ASY_WK_268.ADJUSTMENT_DETAIL A ");
		sb.append("INNER JOIN ");
		sb.append("  ASY_WK_268.PAY_BIZ P ");
		sb.append("ON A.PAY_ID = P.PAY_ID ");
		sb.append("LEFT JOIN ");
		sb.append("  ASY_WK_268.USER_MST UA ");
		sb.append("ON A.AD_FROM = UA.ID ");
		sb.append("LEFT JOIN ");
		sb.append("  ASY_WK_268.USER_MST UB ");
		sb.append("ON A.AD_TO = UB.ID ");
		sb.append("WHERE ");
		sb.append("  A.AD_FROM = 1001 ");
		sb.append("AND ");
		sb.append("  A.AD_TO = 1003 ");
		sb.append("AND ");
		sb.append("  A.AD_DONE <> 1 ");
		sb.append("ORDER BY ");
		sb.append("  AD_DONE DESC, ");
		sb.append("  AD_AMOUNT DESC");

		SELECT_AtoB = sb.toString();
	}

	public CeRestful() throws ClassNotFoundException, SQLException {
		initConn ();
	}

	@GET
	@Path("/MindsCustomerInfoReception/api/v1")
	@Produces(MediaType.APPLICATION_JSON)
	public String initMain(String str) {

		//return "{\"user\":\"あいう\",\"name\":\"陳楊\"}";
//		List <NormalResult> nrList = new ArrayList<NormalResult>();
//		NormalResult nr = new NormalResult ();
//		nr.setId("1001");
//		nr.setName("cy");
//		nrList.add(nr);
//
//		nr = new NormalResult ();
//		nr.setId("1002");
//		nr.setName("lh");
//		nrList.add(nr);
//
//
//		JSONArray object = JSONArray.fromObject(nrList);
//		return object.toString();
		//return "{\"user\":[{\"id\":\"1001\",\"name\":\"陳楊\"},{\"id\":\"1002\",\"name\":\"羅浩\"}],\"pay\":[{\"jd_done_flag\":\"0\",\"pay_id\":\"1001\",\"amount\":\"5000\",\"use\":\"1002\",\"payer_name\":\"陳楊\",\"users_name\":\"羅浩\",\"regist_date\":\"2018年04月19日\"}]}";

		ArrayList <PayList> plList = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
			conn = getConn();
			stmt = conn.prepareStatement(SELECT_SIHARAI);
			rset = stmt.executeQuery();
			plList = new ArrayList<PayList>();
			while (rset.next()) {
				PayList pl = new PayList ();
				pl.setPayId(rset.getBigDecimal("PAY_ID"));
				pl.setPayAmount(rset.getBigDecimal("PAY_AMOUNT"));
				pl.setPayer((String) userMap.get(rset.getBigDecimal("PAY_PAYER").toString()));

				String[] payForUsers = rset.getString("PAY_FOR_USERS").split(",");
				StringBuffer temSb = new StringBuffer();
				for (int j=0;j<payForUsers.length;j++) {
					if (j != 0) {
						temSb.append(",");
					}
					temSb.append(userMap.get((String)payForUsers[j]));
				}
				pl.setPayForusers(temSb.toString());
				pl.setPayContent(rset.getString("PAY_CONTENT"));
				pl.setRegistDate(rset.getString("REGIST_DATE"));

				String[] adFrom = rset.getString("AD_FROM").split(",");
				String[] adDone = rset.getString("AD_DONE").split(",");
				String[] adAmount = rset.getString("AD_AMOUNT").split(",");
				String[] adDate = rset.getString("AD_DATE").split(",");

				ArrayList<Detail> details = new ArrayList<Detail>();

				boolean temFlg = false;
				for (int i=0;i<adFrom.length;i++) {
					Detail tempDetail = new Detail ();
					tempDetail.setName((String)userMap.get(adFrom[i]));
					tempDetail.setAmount(adAmount[i]);
					tempDetail.setDate(adDate[i]);
					tempDetail.setDone(adDone[i]);
					if ("1".equals(adDone[i])) {
						temFlg = true;
					}
					details.add(tempDetail);
				}

				if (temFlg) {
					pl.setAdDone("1");
				} else {
					pl.setAdDone("0");
				}

				pl.setDetails(details);
				plList.add(pl);
			}
		} catch (Exception e) {
        } finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		UserMstAndPayList umapl = new UserMstAndPayList ();
		umapl.setUserMst(userList);
		umapl.setPayList(plList);

		JSONObject object = JSONObject.fromObject(umapl);
		return object.toString();
	}

	@POST
	@Path("/MindsCustomerInfoReception/api/v2")
	@Produces(MediaType.TEXT_PLAIN)
	public String createPayBiz(String str) {

		JSONObject object = JSONObject.fromObject(str);
		PayList bean = (PayList)JSONObject.toBean( object, PayList.class );
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		try {
			conn = getConn();
			stmt = conn.createStatement();

			// PAYIDをシークエンスから取得
			BigDecimal payId = null;
			rset = stmt.executeQuery("select ASY_WK_268.SEQ_PAY_BIZ.nextval PAY_ID from sys.dual");
			while (rset.next()) {
				payId = rset.getBigDecimal("PAY_ID");
			}

			// PAY_BIZ登録処理開始
			pstmt = conn.prepareStatement(INSERT_PAY_BIZ);
			Date nowdate = new Date ();
			SimpleDateFormat sdf1 = new SimpleDateFormat ("YYYY-MM-dd HH:mm:ss");
			String nowdateStr = sdf1.format(nowdate);

			pstmt.setBigDecimal(1, payId);
			pstmt.setBigDecimal(2, new BigDecimal(bean.getPayer()));
			pstmt.setString(3, bean.getPayForusers());
			pstmt.setString(4, bean.getPayContent());
			pstmt.setBigDecimal(5, bean.getPayAmount());
			//pstmt.setString(6, nowdateStr);
			pstmt.executeUpdate();

			// details登録処理開始
			String[] users = bean.getPayForusers().split(",");
			BigDecimal amount = bean.getPayAmount();
			BigDecimal adAdmout = amount.divide(new BigDecimal(users.length), 0, BigDecimal.ROUND_HALF_UP);
			BigDecimal adTo = new BigDecimal(bean.getPayer());
			for (int i=0;i<users.length;i++) {
				if (bean.getPayer().equals(users[i])) {
					continue;
				}
				stmt = null;
				stmt = conn.createStatement();
				BigDecimal adDetailId = null;
				rset = stmt.executeQuery("select ASY_WK_268.SEQ_AD_DETAIL.nextval AD_DETAIL_ID from sys.dual");
				while (rset.next()) {
					adDetailId = rset.getBigDecimal("AD_DETAIL_ID");
				}

				pstmt = null;
				pstmt = conn.prepareStatement(INSERT_AD_DETAIL);
				pstmt.setBigDecimal(1, adDetailId);
				pstmt.setBigDecimal(2, payId);
				pstmt.setBigDecimal(3, adAdmout);
				pstmt.setBigDecimal(4, new BigDecimal(users[i]));
				pstmt.setBigDecimal(5, new BigDecimal(bean.getPayer()));
				//pstmt.setString(6, nowdateStr);
				pstmt.setString(6, "0");
				pstmt.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			return "{\"result\":\"NG\"}";
		} finally {
			try {
				rset.close();
				stmt.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		return "{\"result\":\"OK\"}";
	}

	@POST
	@Path("/MindsCustomerInfoReception/api/v3")
	@Produces(MediaType.TEXT_PLAIN)
	public String getDetailsByUsers(String str) {
		JSONObject object = JSONObject.fromObject(str);
		UserAUserB_SEL_KEY bean = (UserAUserB_SEL_KEY)JSONObject.toBean( object, UserAUserB_SEL_KEY.class );

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		UserAUserB_RESULT_LIST detailListAB = new UserAUserB_RESULT_LIST ();

		try {
			conn = getConn();

			// AtoB
			pstmt = conn.prepareStatement(getSqlSelAtoB(bean.getUserA(), bean.getUserB()));
			int paraIndex = 1;
			if (!StringUtils.isEmpty(bean.getUserA())) {
				pstmt.setBigDecimal(paraIndex, new BigDecimal(bean.getUserA()));
				paraIndex++;
			}
			if (!StringUtils.isEmpty(bean.getUserB())) {
				pstmt.setBigDecimal(paraIndex, new BigDecimal(bean.getUserB()));
			}

			ArrayList<UserAUserB_RESULT> userAtoUserBList = new ArrayList<UserAUserB_RESULT> ();
			UserAUserB_RESULT userAtoUserB = new UserAUserB_RESULT ();
			rset = pstmt.executeQuery();
			while (rset.next()) {
				userAtoUserB = new UserAUserB_RESULT ();
				userAtoUserB.setUserIdFrom(rset.getBigDecimal("AD_FROM"));
				userAtoUserB.setUserIdTo(rset.getBigDecimal("AD_TO"));
				userAtoUserB.setUserNmFrom(rset.getString("NAMEA"));
				userAtoUserB.setUserNmTo(rset.getString("NAMEB"));
				userAtoUserB.setPayNum(rset.getBigDecimal("PAY_ID"));
				userAtoUserB.setAdNum(rset.getBigDecimal("AD_DETAIL_ID"));
				userAtoUserB.setUse(rset.getString("PAY_CONTENT"));
				userAtoUserB.setAmount(rset.getBigDecimal("AD_AMOUNT"));
				userAtoUserBList.add(userAtoUserB);
			}
			detailListAB.setDetailListAB(userAtoUserBList);

			// BtoA
			pstmt = conn.prepareStatement(getSqlSelAtoB(bean.getUserB(), bean.getUserA()));
			paraIndex = 1;
			if (!StringUtils.isEmpty(bean.getUserB())) {
				pstmt.setBigDecimal(paraIndex, new BigDecimal(bean.getUserB()));
				paraIndex++;
			}
			if (!StringUtils.isEmpty(bean.getUserA())) {
				pstmt.setBigDecimal(paraIndex, new BigDecimal(bean.getUserA()));
			}

			ArrayList<UserAUserB_RESULT> userBtoUserAList = new ArrayList<UserAUserB_RESULT> ();
			UserAUserB_RESULT userBtoUserA = new UserAUserB_RESULT ();
			rset = pstmt.executeQuery();
			while (rset.next()) {
				userBtoUserA = new UserAUserB_RESULT ();
				userBtoUserA.setUserIdFrom(rset.getBigDecimal("AD_FROM"));
				userBtoUserA.setUserIdTo(rset.getBigDecimal("AD_TO"));
				userBtoUserA.setUserNmFrom(rset.getString("NAMEA"));
				userBtoUserA.setUserNmTo(rset.getString("NAMEB"));
				userBtoUserA.setPayNum(rset.getBigDecimal("PAY_ID"));
				userBtoUserA.setAdNum(rset.getBigDecimal("AD_DETAIL_ID"));
				userBtoUserA.setUse(rset.getString("PAY_CONTENT"));
				userBtoUserA.setAmount(rset.getBigDecimal("AD_AMOUNT"));
				userBtoUserAList.add(userBtoUserA);
			}
			detailListAB.setDetailListBA(userBtoUserAList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		JSONObject result = JSONObject.fromObject(detailListAB);
		return result.toString();
	}

	@POST
	@Path("/MindsCustomerInfoReception/api/v4")
	@Produces(MediaType.TEXT_PLAIN)
	public String doAdjust(String str) {
		JSONObject object = JSONObject.fromObject(str);
		AdjustParam bean = (AdjustParam)JSONObject.toBean( object, AdjustParam.class );

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;


		try {
			conn = getConn();
			stmt = conn.createStatement();

			// AD_EVENT_IDをシークエンスから取得
			BigDecimal adEventId = null;
			rset = stmt.executeQuery("select ASY_WK_268.SEQ_AD_EVENT.nextval AD_EVENT_ID from sys.dual");
			while (rset.next()) {
				adEventId = rset.getBigDecimal("AD_EVENT_ID");
			}

			// TODO EVENTのinsert、pyファイルの375行目
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	private String getSqlSelAtoB(String userA, String userB) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("  A.AD_DETAIL_ID ");
		sb.append("  ,A.PAY_ID ");
		sb.append("  ,A.AD_AMOUNT ");
		sb.append("  ,UA.NAME NAMEA ");
		sb.append("  ,UB.NAME NAMEB ");
		sb.append("  ,A.AD_DATE ");
		sb.append("  ,A.AD_DONE ");
		sb.append("  ,P.PAY_CONTENT ");
		sb.append("  ,A.AD_FROM ");
		sb.append("  ,A.AD_TO ");
		sb.append("FROM ");
		sb.append("  ASY_WK_268.ADJUSTMENT_DETAIL A ");
		sb.append("INNER JOIN ");
		sb.append("  ASY_WK_268.PAY_BIZ P ");
		sb.append("ON A.PAY_ID = P.PAY_ID ");
		sb.append("LEFT JOIN ");
		sb.append("  ASY_WK_268.USER_MST UA ");
		sb.append("ON A.AD_FROM = UA.ID ");
		sb.append("LEFT JOIN ");
		sb.append("  ASY_WK_268.USER_MST UB ");
		sb.append("ON A.AD_TO = UB.ID ");
		sb.append("WHERE ");
		if (!StringUtils.isEmpty(userA)) {
			sb.append("  A.AD_FROM = ? ");
			sb.append("AND ");
		}
		if (!StringUtils.isEmpty(userB)) {
			sb.append("  A.AD_TO = ? ");
			sb.append("AND ");
		}
		sb.append("  A.AD_DONE <> 1 ");
		sb.append("ORDER BY ");
		sb.append("  AD_DONE DESC, ");
		sb.append("  AD_AMOUNT DESC");
		return sb.toString();
	}

	@GET
	@Path("/MindsCustomerInfoReception/api/v0")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserMst(String str) {
		UserList ul = new UserList ();
		ul.setUserList(userList);
		JSONObject object = JSONObject.fromObject(ul);
		return object.toString();
	}


	@POST
	@Path("/MindsCustomerInfoReception/api/v5")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public HelloResult testJson(@Context HttpHeaders headers) {

		List<String> h = headers.getRequestHeader("x-ces-jwt");
		MultivaluedMap<String, String> map = headers.getRequestHeaders();

		List<String> a = map.get("x-ces-jwt");

		HelloResult result = new HelloResult();
		result.mebr_no="99900213";
		result.cust_id="7765234";
		result.ufpr_mebr_no="001";
		result.able_i_nam="able_i_nam000";
		result.flg="1";
		return result;

	}
}
