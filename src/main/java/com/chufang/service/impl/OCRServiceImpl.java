package com.chufang.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baidu.aip.ocr.AipOcr;
import com.chufang.consts.ChufangConsts;
import com.chufang.service.HttpService;
import com.chufang.service.OCRService;
import net.sf.json.JSONObject;

@Service
public class OCRServiceImpl extends CommonService implements OCRService {

	private final static Logger logger = LoggerFactory.getLogger(OCRServiceImpl.class);
	private final static String OCR_OAUTH_URL = "https://aip.baidubce.com/oauth/2.0/token";
	
	@Value("${ocr.appid}")
	private String OCR_APP_ID;
	
	@Value("${ocr.api.key}")
	private String OCR_API_KEY;
	
	@Value("${ocr.sec.key}")
	private String OCR_SEC_KEY;
	
	@Resource
	private HttpService httpService;
	
//	private Hashtable<String, Long> tokenMap = new Hashtable<String, Long>();
	
	@Override
	public JSONObject processImgFile(String webImgFile) {
		JSONObject rst = generateRst();
//		String accessToken = getAccessToken();
//		if (accessToken == null) {
//			rst.put(ChufangConsts.RC, ChufangConsts.FAIL);
//			rst.put(ChufangConsts.ERR_MSG,"获取token失败");
//			return rst;
//		}
		AipOcr client = new AipOcr(OCR_APP_ID, OCR_API_KEY, OCR_SEC_KEY);
		byte[] contents = extractWebImg(webImgFile);
		if (contents == null) {
			rst.put(ChufangConsts.RC, ChufangConsts.FAIL);
			rst.put(ChufangConsts.ERR_MSG,"获取图片" + webImgFile + "失败");
			return rst;
		}
		HashMap<String, String> config = new HashMap<String, String>();
		config.put("detect_direction", "true");
		org.json.JSONObject response = client.general(contents, config);
		if (response != null && response.get("words_result") != null) {
			rst.put("data", response.toString());
		} else {
			rst.put(ChufangConsts.RC, ChufangConsts.FAIL);
			rst.put(ChufangConsts.ERR_MSG, rst);
			return rst;
		}
		return rst;
	}
	
	private byte[] extractWebImg(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			//打开网络输入流
			DataInputStream dis = new DataInputStream(url.openStream());
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
			int block = 1024;
	        byte[] buff = new byte[block];  
	        int rc = 0;  
	        while ((rc = dis.read(buff, 0, block)) > 0) {  
	            swapStream.write(buff, 0, rc);  
	        }  
	        return swapStream.toByteArray();  
		} catch (Exception e) {
			logger.error("fail to download imgurl = " + imageUrl);
		}
		return null;
	}
	
//	private String getAccessToken() {
//		if (tokenMap.size() > 0) {
//			String key = tokenMap.keySet().iterator().next();
//			if (tokenMap.get(key) > System.currentTimeMillis()) {
//				return key;
//			}
//		}
//		Map<String, Object> parmas = new HashMap<>();
//		parmas.put("grant_type", "client_credentials");
//		parmas.put("client_id", OCR_API_KEY);
//		parmas.put("client_secret", OCR_SEC_KEY);
//		JSONObject rst = httpService.processRequestStr(OCR_OAUTH_URL, parmas);
//		if (rst != null && rst.containsKey("access_token")) {
//			//{"access_token":"24.0679389a8d7883eebf9b27e751132f69.2592000.1504453672.282335-9928019","session_key":"9mzdCy4pSjMX00pNtYyDD102fD2UUJv97JM7mjSQsIUvCIQRzvt7/QgXuM0blvMgL4adDMopeQEFqz7aTUyhcy5bH8yQ","scope":"public vis-ocr_ocr brain_ocr_scope brain_ocr_general brain_ocr_general_basic brain_ocr_general_enhanced brain_ocr_webimage brain_all_scope brain_ocr_idcard brain_ocr_driving_license brain_ocr_vehicle_license vis-ocr_plate_number brain_solution brain_ocr_plate_number brain_ocr_accurate brain_ocr_accurate_basic wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian wangrantest_test wangrantest_test1 bnstest_test1 bnstest_test2 vis-classify_flower","refresh_token":"25.c96f96d4f3e16f59e68e19b94a2f2008.315360000.1817221672.282335-9928019","session_secret":"e6dae9a8f9bc8db3bd52422b5863238e","expires_in":2592000}
//			tokenMap.put(rst.getString("access_token"), (rst.getLong("expires_in")-1) *1000);
//			return rst.getString("access_token");
//		} else {
//			logger.error("fail to refresh token. rst=(" + rst + ")");
//		}
//		return null;
//	}

	@Override
	public String accessToken() {
		return "test access token";
	}

}
