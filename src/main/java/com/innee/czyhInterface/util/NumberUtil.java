package com.innee.czyhInterface.util;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class NumberUtil {

	private static long serial = 0L;

	private static long otherSerial = 0L;

	public static synchronized String getOrderNum(int cityId) {
		StringBuilder number = new StringBuilder();
		serial = serial + RandomUtils.nextInt(1, 5);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		// 设置是否使用分组
		nf.setGroupingUsed(false);
		// 设置最大整数位数
		nf.setMaximumIntegerDigits(5);
		// 设置最小整数位数
		nf.setMinimumIntegerDigits(5);
		number.append(cityId).append(DateFormatUtils.format(new Date(), "yyyyMMdd").substring(3, 8))
				.append(nf.format(serial));
		return number.toString();
	}

	public static void setSerial(long seriala) {
		serial = seriala;
	}

	public static synchronized String getOtherOrderNum(int cityId, String prefix) {
		StringBuilder number = new StringBuilder();
		otherSerial = otherSerial + RandomUtils.nextInt(1, 5);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		// 设置是否使用分组
		nf.setGroupingUsed(false);
		// 设置最大整数位数
		nf.setMaximumIntegerDigits(5);
		// 设置最小整数位数
		nf.setMinimumIntegerDigits(5);
		number.append(prefix).append(cityId).append(DateFormatUtils.format(new Date(), "yyyyMMdd").substring(3, 8))
				.append(nf.format(otherSerial));
		return number.toString();
	}

	public static void setOtherSerial(long otherSeriala) {
		otherSerial = otherSeriala;
	}

	public static String orderNumToManualCode(String orderNum) {
		if (StringUtils.isBlank(orderNum) || orderNum.length() < 11) {
			return StringUtils.EMPTY;
		}
		StringBuilder manualCode = new StringBuilder();

		String ona = orderNum.substring(0, 2);
		String onb = orderNum.substring(2, 4);
		String onc = orderNum.substring(4, 6);
		String ond = orderNum.substring(6, 8);
		String one = orderNum.substring(8, 11);

		long orderNuma = Long.parseLong(ona, 10);
		long orderNumb = Long.parseLong(onb, 10);
		long orderNumc = Long.parseLong(onc, 10);
		long orderNumd = Long.parseLong(ond, 10);
		long orderNume = Long.parseLong(one, 10);

		String orderNumStr2a = Long.toBinaryString(orderNuma);
		String orderNumStr2b = Long.toBinaryString(orderNumb);
		String orderNumStr2c = Long.toBinaryString(orderNumc);
		String orderNumStr2d = Long.toBinaryString(orderNumd);
		String orderNumStr2e = Long.toBinaryString(orderNume);

		manualCode.append(Integer.toString(Integer.parseInt(orderNumStr2a, 2)))
				.append(Integer.toString(Integer.parseInt(orderNumStr2b, 2)))
				.append(Integer.toString(Integer.parseInt(orderNumStr2c, 2)))
				.append(Integer.toString(Integer.parseInt(orderNumStr2d, 2)))
				.append(Integer.toString(Integer.parseInt(orderNumStr2e, 2)));

		return manualCode.toString();
	}

	public static String manualCodeToOrderNum(String manualCode) {
		if (StringUtils.isBlank(manualCode)) {
			return StringUtils.EMPTY;
		}
		StringBuilder orderNum = new StringBuilder();

		long manualCode2 = Long.parseLong(manualCode, 10);

		String manualCodeStr2 = Long.toBinaryString(manualCode2);

		String sta = manualCodeStr2.substring(0, 6);
		String stb = manualCodeStr2.substring(6, 10);
		String stc = manualCodeStr2.substring(10, 15);
		String std = manualCodeStr2.substring(12, 14);
		String ste = manualCodeStr2.substring(14, 5);

		orderNum.append(Integer.toString(Integer.parseInt(sta, 2))).append(Integer.toString(Integer.parseInt(stb, 2)))
				.append(Integer.toString(Integer.parseInt(stc, 2))).append(Integer.toString(Integer.parseInt(std, 2)))
				.append(Integer.toString(Integer.parseInt(ste, 2)));

		return orderNum.toString();
	}
	
	public static synchronized String getVerificationCode() {
		StringBuilder number = new StringBuilder();
		
		Calendar Cld = Calendar.getInstance();
		int mm = Cld.get(Calendar.MINUTE);
		int MI = Cld.get(Calendar.MILLISECOND);
		if(mm<10){
			mm = mm+60;
		}
		if(MI<100){
			MI = MI+100;
		}
		
		number.append(mm).append(RandomUtils.nextInt(0, 9)).append(RandomUtils.nextInt(0, 9))
				.append(RandomUtils.nextInt(0, 9)).append(RandomUtils.nextInt(0, 9))
				.append(MI);
		return number.toString();
	}


	public static void main(String[] args) throws Exception {
		// String st = "4cf904ab-ebed-4298-9c5d-416929cf18b9###";
		// String key = "123456";
		// StringBuilder allKey = new StringBuilder();
		// for (int i = 0; i < 3; i++) {
		// allKey.append(key);
		// }
		// key = allKey.substring(0, 16).toString();
		// System.out.println("aaaaaaaaaaaaaaaa " + key);
		// AESCrypto aes = AESCrypto.getInstance();
		//
		// SecretKey sk = aes.generateKey(key.getBytes());
		//
		// byte[] result = aes.encrypt(st.getBytes(), sk);
		// String res = AESCrypto.byteToHex(result);
		// System.out.println("bbbbbbbbbbbbbbbb " + res);
		// byte[] newresult = AESCrypto.hexToBytes(res);
		//
		// byte[] src = aes.decrypt(newresult, sk);
		// System.out.println("cccccccccccccccc " + st);
		// System.out.println("dddddddddddddddd " +
		// StringUtils.toEncodedString(src, null));
		// System.out.println(Integer.valueOf("aacf45", 16));
		// System.out.println(Integer.toHexString(11194181));
		// System.out.println(Long.toHexString(16032601835L));
		// System.out.println(URLDecoder.decode(
		// "http%3A%2F%2Fwx.jiaoyoubaobao.com%2Frtrip%2Flanding%3Fspm%3Dmenu.default.rtriplanding.0",
		// "UTF-8"));

		// byte[] resultaa = Cryptos.aesEncrypt(st.getBytes(), key.getBytes());

		// res = AESCrypto.byteToHex(resultaa);
		// System.out.println("eeeeeeeeeeeeeeee " + res);
		// System.out.println("ffffffffffffffff " +
		// StringUtils.toEncodedString(resultaa, null));
		// System.out.println("gggggggggggggggg "
		// +
		// Cryptos.aesDecrypt("407881bc5fa53a59fde878b7fb570fc11272297350860d1fa3555e61324c9f15".getBytes(),
		// "0002100002100002".getBytes()));

		// AESCrypto aes = AESCrypto.getInstance();
		// SecretKey sk = aes.generateKey("0001430001430001".getBytes());
		// byte[] result =
		// AESCrypto.hexToBytes("1ebebe1937083447b3c5f916ad144aabe898880a3b4778b1a290688e027fb1ec");
		// byte[] src = aes.decrypt(result, sk);
		// System.out.println(StringUtils.toEncodedString(src, null));
		// System.out.println(FastDateFormat.getInstance().format(new Date()));
		// long s = System.currentTimeMillis();
		//
		 for(int i=0;i<=10000;i++){
			 System.out.println(getVerificationCode());
		 }
//		String a = NumberUtil.orderNumToManualCode("16071300657");
//		// 167573113061
//
//		System.out.println(a);
//		String b = NumberUtil.manualCodeToOrderNum(a);
		
	}
}
