package com.chufang.test.service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectResult;
import com.chufang.consts.ChufangConsts;
import com.chufang.service.DuizhangService;
import com.chufang.service.HelloService;
import com.chufang.service.HttpService;
import com.chufang.service.OCRService;
import com.chufang.service.YG_TiaoJiCFService;
import com.chufang.test.BaseTest;

import net.sf.json.JSONObject;

public class HelloTest extends BaseTest {
	
	@Resource
	private HelloService helloService;
	
	@Resource
	private OCRService ocrService;
	
	@Resource
	private HttpService httpService;
	
	@Resource
	private DuizhangService duizhangService;
	
	@Value("${yuyi.parse.host}")
	private String yiyiHost;
	
	@Resource
	private YG_TiaoJiCFService tiaojicfService;
	
	public static void main(String[] arg) throws Exception  {
		System.out.println(DigestUtils.md5Hex("wuhualiang"));
		String endpoint = "http://%soss-cn-hangzhou.aliyuncs.com";
		String accessKeyId = "LTAI5abehXGjwUAJ", accessKeySecret = "pbxMW2oIpUwVNJ0qwICLkXJiSafDFM";
		 ClientConfiguration conf = new ClientConfiguration();
		    // 开启支持CNAME选项
		    conf.setSupportCname(true);
	        String bucketName = "ysf-chufang";
	        String objName = UUID.randomUUID().toString()+".jpg";
	        FileInputStream in = new FileInputStream(new File("d:/010f485d-0b15-49e2-935c-98ef210fe713.jpg"));
		    try {
			    // 创建OSSClient实例
			    OSSClient client = new OSSClient(String.format(endpoint,""), accessKeyId, accessKeySecret);
			    PutObjectResult putRst = client.putObject(bucketName,objName, in);
			    System.out.println(putRst);
			    client.setObjectAcl(bucketName, objName, CannedAccessControlList.Private);
			    // 设置URL过期时间为1小时
			    Date expiration = new Date(new Date().getTime() + 1000*60*5L);
			    // 生成URL
			    URL url = client.generatePresignedUrl(bucketName, objName, expiration);
			    System.out.println(url.toString());
			   System.out.println(String.format(endpoint,bucketName + ".") + "/" +  objName);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    } finally {
		    	if (in != null) {
		    		in.close();
		    	}
		    }
	}
	
//	@Test
	public void insert() {
		com.chufang.entity.Test test = new com.chufang.entity.Test();
		test.setBirth(15);;
		test.setCreatetime(new Date());
		test.setName(System.currentTimeMillis() + "");
		test.setWeight(114.44d);
		System.out.println(helloService.insert(test));
	}
	
//	@Test
	public void update() {
		System.out.println(helloService.update(1L, "111111new name"));
	}
	
//	@Test
	public void query() {
		
		System.out.println(helloService.query("111111new name"));
	}
	
	@Test
	public void testToken() throws Exception{
//		JSONObject rst = ocrService.processImgFile("http://ysf-chufang.oss-cn-hangzhou.aliyuncs.com/7ba96364c24440f1849591652eca23c9.png");
//		if (rst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
		String text = "{\"log_id\":2293427012,\"words_result\":[{\"words\":\"处方编号:212630\"},{\"words\":\"处方日期:2017年07月09日\"},{\"words\":\"姓名:殷粉粉性别:女年龄:47岁费用类别:职工在职\"},{\"words\":\"门诊病历号:A22837242\"},{\"words\":\"就诊科室:中医科\"},{\"words\":\"联系地址:浙江中烟工业有限责任公司杭州卷炬联系电话:13819451520\"},{\"words\":\"临诊断;结膜炎,肝肾亏虚\"},{\"words\":\"脉案\"},{\"words\":\" Rp\"},{\"words\":\"柴胡\"},{\"words\":\"10g\"},{\"words\":\"防风\"},{\"words\":\"10g\"},{\"words\":\"青箱子10g\"},{\"words\":\"千里光\"},{\"words\":\"10g\"},{\"words\":\"密蒙花10g\"},{\"words\":\"枸杞子15g\"},{\"words\":\"生白术\"},{\"words\":\"30g\"},{\"words\":\"阳春砂6g后下炒枳实\"},{\"words\":\"g\"},{\"words\":\"当归\"},{\"words\":\"10g\"},{\"words\":\"决明子\"},{\"words\":\"15g\"},{\"words\":\"浙贝母15g\"},{\"words\":\"牡蛎\"},{\"words\":\"30g\"},{\"words\":\"蜜甘草6g\"},{\"words\":\"泽泻\"},{\"words\":\"10g\"},{\"words\":\"骨碎补\"},{\"words\":\"15g\"},{\"words\":\"续断\"},{\"words\":\"10g\"},{\"words\":\"杜仲\"},{\"words\":\"10\"},{\"words\":\"g\"},{\"words\":\"贴数:7共:18味医生签名:许立珊\"}],\"words_result_num\":40}";
//		String text = "%7B%20%22errno%22:%200,%20%22msg%22:%20%22success%22,%20%22data%22:%20%7B%20%22direction%22:%200,%20%22words_result_num%22:%2039,%20%22words_result%22:%20[%20%7B%20%22words%22:%20%22%E5%A4%84%E6%96%B9%E7%BC%96%E5%8F%B7:212630%22,%20%22probability%22:%20%7B%20%22variance%22:%200.00399,%20%22average%22:%200.976711,%20%22min%22:%200.77768%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E5%A4%84%E6%96%B9%E6%97%A5%E6%9C%9F:2017%E5%B9%B407%E6%9C%8809%E6%97%A5%22,%20%22probability%22:%20%7B%20%22variance%22:%200.002022,%20%22average%22:%200.979999,%20%22min%22:%200.820907%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E5%A7%93%E5%90%8D:%E6%AE%B7%E7%B2%89%E7%B2%89%E6%80%A7%E5%88%AB:%E5%A5%B3%E5%B9%B4%E9%BE%84:47%E5%B2%81%E8%B4%B9%E7%94%A8%E7%B1%BB%E5%88%AB:%E8%81%8C%E5%B7%A5%E5%9C%A8%E8%81%8C%22,%20%22probability%22:%20%7B%20%22variance%22:%200.001169,%20%22average%22:%200.99061,%20%22min%22:%200.828722%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E9%97%A8%E8%AF%8A%E7%97%85%E5%8E%86%E5%8F%B7:A22837242%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000021,%20%22average%22:%200.996416,%20%22min%22:%200.98376%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E5%B0%B1%E8%AF%8A%E7%A7%91%E5%AE%A4:%E4%B8%AD%E5%8C%BB%E7%A7%91%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999003,%20%22min%22:%200.998094%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E8%81%94%E7%B3%BB%E5%9C%B0%E5%9D%80:%E6%B5%99%E6%B1%9F%E4%B8%AD%E7%83%9F%E5%B7%A5%E4%B8%9A%E6%9C%89%E9%99%90%E8%B4%A3%E4%BB%BB%E5%85%AC%E5%8F%B8%E6%9D%AD%E5%B7%9E%E5%8D%B7%E7%82%AC%E8%81%94%E7%B3%BB%E7%94%B5%E8%AF%9D:13819451520%22,%20%22probability%22:%20%7B%20%22variance%22:%200.005134,%20%22average%22:%200.98072,%20%22min%22:%200.560875%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E4%B8%B4%E5%AE%8B%E8%AF%8A%E6%96%AD:%E7%BB%93%E8%86%9C%E7%82%8E,%E8%82%9D%E8%82%BE%E4%BA%8F%E8%99%9A%22,%20%22probability%22:%20%7B%20%22variance%22:%200.00089,%20%22average%22:%200.981469,%20%22min%22:%200.897675%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E8%84%89%E6%A1%88%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000015,%20%22average%22:%200.995961,%20%22min%22:%200.99207%20%7D%20%7D,%20%7B%20%22words%22:%20%22R%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999906,%20%22min%22:%200.999906%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E6%9F%B4%E8%83%A1%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999409,%20%22min%22:%200.998843%20%7D%20%7D,%20%7B%20%22words%22:%20%2210%22,%20%22probability%22:%20%7B%20%22variance%22:%200.002393,%20%22average%22:%200.950169,%20%22min%22:%200.901256%20%7D%20%7D,%20%7B%20%22words%22:%20%2210%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000154,%20%22average%22:%200.913454,%20%22min%22:%200.901032%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E9%9D%92%E7%AE%B1%E5%AD%90%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000006,%20%22average%22:%200.997878,%20%22min%22:%200.994553%20%7D%20%7D,%20%7B%20%22words%22:%20%22g%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999121,%20%22min%22:%200.999121%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E5%8D%83%E9%87%8C%E5%85%89%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000106,%20%22average%22:%200.992392,%20%22min%22:%200.977812%20%7D%20%7D,%20%7B%20%22words%22:%20%2210g%22,%20%22probability%22:%20%7B%20%22variance%22:%200.025665,%20%22average%22:%200.882418,%20%22min%22:%200.655912%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E5%AF%86%E8%92%99%E8%8A%B1%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999889,%20%22min%22:%200.999784%20%7D%20%7D,%20%7B%20%22words%22:%20%2210%22,%20%22probability%22:%20%7B%20%22variance%22:%200.002348,%20%22average%22:%200.944827,%20%22min%22:%200.89637%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E6%9E%B8%E6%9D%9E%E5%AD%9015g%22,%20%22probability%22:%20%7B%20%22variance%22:%200.003731,%20%22average%22:%200.970748,%20%22min%22:%200.834336%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E7%94%9F%E7%99%BD%E6%9C%AF%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000002,%20%22average%22:%200.998712,%20%22min%22:%200.996804%20%7D%20%7D,%20%7B%20%22words%22:%20%2230g%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000007,%20%22average%22:%200.99791,%20%22min%22:%200.994134%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E9%98%B3%E6%98%A5%E7%A0%826g%E5%90%8E%E4%B8%8B%E7%82%92%E6%9E%B3%E5%AE%9E10g%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000001,%20%22average%22:%200.99937,%20%22min%22:%200.996186%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E5%BD%93%E5%BD%92%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999694,%20%22min%22:%200.999423%20%7D%20%7D,%20%7B%20%22words%22:%20%2210g%22,%20%22probability%22:%20%7B%20%22variance%22:%200.008884,%20%22average%22:%200.9326,%20%22min%22:%200.799308%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E5%86%B3%E6%98%8E%E5%AD%90%22,%20%22probability%22:%20%7B%20%22variance%22:%200.037242,%20%22average%22:%200.861605,%20%22min%22:%200.588693%20%7D%20%7D,%20%7B%20%22words%22:%20%2215g%22,%20%22probability%22:%20%7B%20%22variance%22:%200.002005,%20%22average%22:%200.968235,%20%22min%22:%200.904914%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E6%B5%99%E8%B4%9D%E6%AF%8D%22,%20%22probability%22:%20%7B%20%22variance%22:%200.001391,%20%22average%22:%200.972936,%20%22min%22:%200.920204%20%7D%20%7D,%20%7B%20%22words%22:%20%22%205%20g%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%201,%20%22min%22:%201%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E7%89%A1%E8%9B%8E%22,%20%22probability%22:%20%7B%20%22variance%22:%200.001046,%20%22average%22:%200.967637,%20%22min%22:%200.935293%20%7D%20%7D,%20%7B%20%22words%22:%20%2230%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.998287,%20%22min%22:%200.997985%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E8%9C%9C%E7%94%98%E8%8D%89%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000004,%20%22average%22:%200.99813,%20%22min%22:%200.995498%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E6%B3%BD%E6%B3%BB%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999951,%20%22min%22:%200.99991%20%7D%20%7D,%20%7B%20%22words%22:%20%22g%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999998,%20%22min%22:%200.999998%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E7%A2%8E%E8%A1%A5%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.99976,%20%22min%22:%200.999609%20%7D%20%7D,%20%7B%20%22words%22:%20%22g%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999996,%20%22min%22:%200.999996%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E7%BB%AD%E6%96%AD%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999451,%20%22min%22:%200.999269%20%7D%20%7D,%20%7B%20%22words%22:%20%2210g%22,%20%22probability%22:%20%7B%20%22variance%22:%200.002153,%20%22average%22:%200.966005,%20%22min%22:%200.900393%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E6%9D%9C%E4%BB%B2%22,%20%22probability%22:%20%7B%20%22variance%22:%200,%20%22average%22:%200.999692,%20%22min%22:%200.999464%20%7D%20%7D,%20%7B%20%22words%22:%20%22%E8%B4%B4%E6%95%B0:7%E5%85%B1:18%E5%91%B3%E5%8C%BB%E7%94%9F%E7%AD%BE%E5%90%8D:%E8%AE%B8%E7%AB%8B%E7%8F%8A%22,%20%22probability%22:%20%7B%20%22variance%22:%200.000004,%20%22average%22:%200.999172,%20%22min%22:%200.991847%20%7D%20%7D%20]%20%7D%20%7D";
			JSONObject param = JSONObject.fromObject(text);
			System.out.println(httpService.processRequest(yiyiHost, param));
//		}
	}
	
//	@Test
	public void testTiaoji() {
		System.out.println(tiaojicfService.getDianziCf(1, 1));
	}
	
}
