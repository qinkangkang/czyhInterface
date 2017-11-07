package com.innee.czyhInterface.util.pingPay.util;

import org.apache.commons.codec.binary.Base64;

import com.innee.czyhInterface.util.PropertiesUtil;
import com.taobao.api.DefaultTaobaoClient;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class PingPPUtil {

	public static String apiKey = PropertiesUtil.getProperty("pingApiKey");

	public static String appId = PropertiesUtil.getProperty("pingAppId");

	 // 你生成的私钥路径
    public final static String privateKey = "MIICXgIBAAKBgQDTFMdE9//0sdqfbc12vx+VVxzrKJ8jmCI6iB/QSET4br2zYBdr\n" +
            "OqIL4+CgQqhRCirRxK9bNlxGi5oEfJen+Jxnbul5CzjAGfOxGS7/XBMbnDAS4tyS\n" +
            "Q5sD8SccvPi8vcg99/swB5jn5OdVNXdxCfvpLOFMagFK9WHrd5r8C3HsqwIDAQAB\n" +
            "AoGBAIQgaoe6cUZ7Ge1X7xfYlO2HRPvSRqMiNLLMyJU416tmrmocySv+3LZyowXO\n" +
            "E8v+gEUkR9vhkCs8cWFDmC61iTh1NJ6Km0iAkx9Hq2dYgfA6HehubZRewcKJDTab\n" +
            "TVh73FQv6Dtob+Mkd9j4qDcqhrJfK0348jtKOk5biTR7AsARAkEA/8HdXMNCcfU2\n" +
            "BJxnh4EUSJsgUb5q6lmCG8Xi+P4NhqAaA4APwnhLZ2e5iTJJk+sFMCDFAoJOjCBi\n" +
            "Avrl5qTjwwJBANNID074K6CFOXUlEu3Xn4xbLTytyLeUKNJUbcBJA+pGtj20BimJ\n" +
            "ZxnznuPLyrr9+GaHZaxgVoA/h7WQ0O4bzPkCQQCzULwgNrKWbYMTFomrwr9y7Hhr\n" +
            "d6NEbb9AM60gQBdBpWy3uYjTIz5S4LF/1Kcrs2KuzLr0OGJT8rxlNH4OCNu7AkEA\n" +
            "xZ89UmwmwnpaiAwpbHanMa2G3p7eg3f9xt5Z2eVx2AxUIdrleKyg11FcaU4RTl6H\n" +
            "nb8/hsCFqGfEFDxkcJNoYQJAJBUkmo+pgYnnthMzChdtnQSgLv7GBe3a0rsCz4UU\n" +
            "SwowP9pRNvVg2FJ4RFhMkDIJ35e8ZVEZAl10E9MaUsEcEQ==\n";

    private static final String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2iBC44IgDq1pPbOH2cgv\n" +
            "O+6sNVw4vXwBaToW6JxMBFvu4nRcip0MMqEjt6BSfiB1XEatQ8e1X4hYFf5nfioU\n" +
            "Fe9s95vHRf2hyYDN0V8iwekTv5uW2V2FtbzaB358ssIoPfj17lbn/iU0lBverI+P\n" +
            "8frR/mdmqhxWQNCBXubs5xk3BrBOpVOZkNanVpQUdmRnX5JjXdfFc5A2wbGCWDDK\n" +
            "t3HrSezLOdZqEofQ9FkXrTy8hBB6wOonl48xv2v2W8F3HLQ81Mny1sh1bO/FDZAO\n" +
            "r6SoO8f54a+plBEs+qM/+GrR/ehAhU0gK0ZUnrceA7gLDe61CschKiDxysBSGxcf\n" +
            "SQIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

	public final static String getChannel(Integer payType) {

		String resPayType = null;
		if (payType.intValue() == 20) {
			resPayType = "alipay";
		} else if (payType.intValue() == 30) {
			resPayType = "wx";
		}
		return resPayType;
	}

	public static String getSignatureString(HttpServletRequest request) {
		return request.getHeader("x-pingplusplus-signature");
	}

	/**
	 * 获得公钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPubKey() {
		String pubKeyString = publicKey;
		pubKeyString = pubKeyString.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
		byte[] keyBytes = Base64.decodeBase64(pubKeyString);

		// generate public key
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = null;
		PublicKey publicKey = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			// throw new ParamException("无此签名");
		}

		try {
			publicKey = keyFactory.generatePublic(spec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			// throw new ParamException("无效的公钥");
		}
		return publicKey;
	}

	/**
	 * 验证签名
	 * 
	 * @param dataString
	 * @param signatureString
	 * @param publicKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static boolean verifyData(String dataString, String signatureString, PublicKey publicKey) {
		byte[] signatureBytes = Base64.decodeBase64(signatureString);
		Signature signature = null;
		try {
			signature = Signature.getInstance("SHA256withRSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			signature.initVerify(publicKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			// throw new ParamException("无效的签名");
		}
		try {
			signature.update(dataString.getBytes("UTF-8"));
		} catch (SignatureException e) {
			e.printStackTrace();
			// throw new ParamException("签名异常");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// throw new ParamException("无效的编码格式");
		}
		boolean result = false;
		try {
			result = signature.verify(signatureBytes);
		} catch (SignatureException e) {
			e.printStackTrace();
			// throw new ParamException("验证签名异常");
		}
		return result;
	}
}
