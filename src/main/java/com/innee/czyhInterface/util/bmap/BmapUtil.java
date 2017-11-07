package com.innee.czyhInterface.util.bmap;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.Native2AsciiUtil;
import com.innee.czyhInterface.util.log.OutPutLogUtil;

public class BmapUtil {

	private static final Logger logger = LoggerFactory.getLogger(BmapUtil.class);

	// PI = 3.14159265359
	private final static double DEF_PI = Math.PI; // PI = 3.14159265359
	// 2*PI = 6.28318530712
	private final static double DEF_2PI = 2 * Math.PI;
	// PI/180.0 = 0.01745329252
	private final static double DEF_PI180 = Math.PI / 180;
	// radius of earth
	private final static double DEF_R = 6370693.5;

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	/**
	 * 计算两个坐标点短距离
	 * 
	 * @param lon1
	 *            基点坐标经度
	 * @param lat1
	 *            基点坐标纬度
	 * @param lon2
	 *            目标坐标经度
	 * @param lat2
	 *            目标坐标纬度
	 * @return
	 */
	public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI)
			dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
			dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	/**
	 * 计算两个坐标点长距离
	 * 
	 * @param lon1
	 *            基点坐标经度
	 * @param lat1
	 *            基点坐标纬度
	 * @param lon2
	 *            目标坐标经度
	 * @param lat2
	 *            目标坐标纬度
	 * @return
	 */
	public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

	public static String updateOrderGps(String ip, Logger logger) {
		LinkedHashMap<String, Object> paramsMap = Maps.newLinkedHashMap();
		String gps = "";
		try {
			paramsMap.put("ak", LbsCloud.AK);
			paramsMap.put("coor", LbsCloud.coor);
			paramsMap.put("ip", ip);

			String sn = LbsCloud.getSn(LbsCloud.LOCATION_URI_IP, paramsMap);
			paramsMap.put("sn", sn);

			String json = HttpClientUtil.callUrlGet(LbsCloud.LBS_URL + LbsCloud.LOCATION_URI_IP, paramsMap);
			BmapIpLocationBean bmapIpLocationBean = mapper.fromJson(json, BmapIpLocationBean.class);
			// 返回状态码如果为0，表示获取活动距离成功
			if (bmapIpLocationBean.getStatus() == 0) {
				BmapIpLocationBean.Content.Point point = bmapIpLocationBean.getContent().getPoint();
				gps = new StringBuilder().append(point.getX()).append(",").append(point.getY()).toString();
			} else {
				bmapIpLocationBean.setMessage(Native2AsciiUtil.ascii2Native(bmapIpLocationBean.getMessage()));
				String errorInfo = new StringBuilder().append("去百度LBS云根据IP地址和获取坐标信息时出错，状态码：")
						.append(bmapIpLocationBean.getStatus()).append("；提示信息：").append(bmapIpLocationBean.getMessage())
						.append("；IP：").append(ip).toString();
				logger.error(errorInfo);
				throw new ServiceException(errorInfo);
			}
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ip", ip);
			OutPutLogUtil.printLoggger(e, map, logger);
		}
		return gps;
	}

	public static String getGpsByIp(String ip) {
		String clientGPS = null;
		if (!(ip.equals("127.0.0.1") || ip.startsWith("10.") || ip.startsWith("192.168."))) {
			LinkedHashMap<String, Object> paramsMap = Maps.newLinkedHashMap();
			paramsMap.put("ak", LbsCloud.AK);
			paramsMap.put("coor", LbsCloud.coor);
			paramsMap.put("ip", ip);

			try {
				String sn = LbsCloud.getSn(LbsCloud.LOCATION_URI_IP, paramsMap);
				paramsMap.put("sn", sn);
				String json = HttpClientUtil.callUrlGet(LbsCloud.LBS_URL + LbsCloud.LOCATION_URI_IP, paramsMap);

				BmapIpLocationBean bmapIpLocationBean = mapper.fromJson(json, BmapIpLocationBean.class);
				// 返回状态码如果为0，表示获取活动距离成功
				if (bmapIpLocationBean.getStatus() == 0) {
					BmapIpLocationBean.Content.Point point = bmapIpLocationBean.getContent().getPoint();
					clientGPS = new StringBuilder().append(point.getX()).append(",").append(point.getY()).toString();
				} else {
					bmapIpLocationBean.setMessage(Native2AsciiUtil.ascii2Native(bmapIpLocationBean.getMessage()));
					String errorInfo = new StringBuilder().append("去百度LBS云根据IP地址和获取坐标信息时出错，状态码：")
							.append(bmapIpLocationBean.getStatus()).append("；提示信息：")
							.append(bmapIpLocationBean.getMessage()).append("；IP：").append(ip).toString();
					throw new ServiceException(errorInfo);
				}
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			}
		}
		return clientGPS;
	}

	
	public static String[] gaoDeToBaidu(double gd_lon, double gd_lat) {//经度，纬度 
	    String[] bd_lat_lon = new String[2];
	    double PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double x = gd_lon, y = gd_lat;
	    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
	    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
	    bd_lat_lon[0] = String.valueOf(z * Math.cos(theta) + 0.0065);
	    bd_lat_lon[1] = String.valueOf(z * Math.sin(theta) + 0.006);
	    return bd_lat_lon;
	}
	
	public static String baiduToGaoDe(String baiduGps) {//经度，纬度 
	    String[] gd_lat_lon = new String[2];
	    DecimalFormat df   = new DecimalFormat("######0.00000");
	    double PI = 3.14159265358979324 * 3000.0 / 180.0;
	    double x = Double.valueOf(baiduGps.split(",")[0]) - 0.0065;
	    double y = Double.valueOf(baiduGps.split(",")[1]) - 0.006;
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
	    gd_lat_lon[0] = String.valueOf(df.format(z * Math.cos(theta)));
	    gd_lat_lon[1] = String.valueOf(df.format(z * Math.sin(theta)));
	    return gd_lat_lon[0]+","+gd_lat_lon[1];
	}
	
	public static void main(String[] args) {
		System.out.println(BmapUtil.baiduToGaoDe("116.46396,39.919611"));
	}
	

}