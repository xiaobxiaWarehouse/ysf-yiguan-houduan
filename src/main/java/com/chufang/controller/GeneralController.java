package com.chufang.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.chufang.annotation.NoAuth;
import com.chufang.consts.ChufangConsts;
import com.chufang.service.AuthService;
import com.chufang.service.OSSService;
import com.chufang.service.YG_TiaoJiCFService;
import com.chufang.util.CookieUtil;
import com.chufang.util.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/general")
public class GeneralController extends BaseController {

	private static String codeChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Resource
	private AuthService authService;
	
	@Resource
	private OSSService ossService;
	
	@Resource
	private YG_TiaoJiCFService tiaojicfService;
	
	@RequestMapping(value = "/pic/vcode/get")
	@NoAuth
	public void picVcodeGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 因此，为了保险起见，同时使用这3条语句来关闭浏览器的缓冲区
		response.setHeader("ragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 设置图形验证码的长和宽
		int width = 90, height = 30;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandomColor(180, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, height));
		g.setColor(getRandomColor(120, 180));
		// 用户保存最后随机生成的验证码
		StringBuffer validationCode = new StringBuffer();
		String[] fontNames = { "Times New Roman", "Book antiqua", "Arial" };

		// 随机生成4个验证码
		for (int i = 0; i < 4; i++) {
			// 随机设置当前验证码的字符的字体
			g.setFont(new Font(fontNames[random.nextInt(3)], Font.ITALIC, height-random.nextInt(10)));
			// 随机获得当前验证码的字符
			char codeChar = codeChars.charAt(random.nextInt(codeChars.length()));
			validationCode.append(codeChar);
			// 随机设置当前验证码字符的颜色
			g.setColor(getRandomColor(10, 100));
			// 在图形上输出验证码字符，x和y都是随机生成的
			g.drawString(String.valueOf(codeChar), 20 * i + random.nextInt(9), height-random.nextInt(10));
		}
		// 获得HttpSession对象
//		response.addCookie(CookieUtil.makeCookie(ChufangConsts.VCODE_KEY, new AES().aesEncrypt(validationCode.toString(), null), false, "www.chufang.com", 5*60));
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(5 * 60);
		// 将验证码保存在session对象中,key为validation_code
		session.setAttribute(ChufangConsts.VCODE_KEY, validationCode.toString());
		// 关闭Graphics对象
		g.dispose();
		OutputStream outS = response.getOutputStream();
		ImageIO.write(image, "JPEG", outS);
	}
	
	@RequestMapping(value = "/vcode/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject vcodeGet(HttpServletRequest request, HttpServletResponse response, 
			String shoujihm) throws Exception {
		String validationCode = StringUtil.getRandomNumString(6);
		JSONObject rst = authService.sendVcode(shoujihm, validationCode, false);
		if (rst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
			// 获得HttpSession对象
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(5 * 60);
			// 将验证码保存在session对象中,key为validation_code
			session.setAttribute(ChufangConsts.VCODE_KEY2, validationCode.toString());
//			CookieUtil.makeCookie(ChufangConsts.VCODE_KEY2, new AES().aesEncrypt(validationCode.toString(), null), false, "", 5*60);
		}
		return wrapResult(rst);
	}
	
	@RequestMapping(value = "/vcode/get/v2", method = RequestMethod.POST)
	@NoAuth
	public @ResponseBody JSONObject vcodeGetV2(HttpServletRequest request, HttpServletResponse response, 
			String shoujihm) throws Exception {
		String validationCode = StringUtil.getRandomNumString(6);
		JSONObject rst = authService.sendVcode(shoujihm, validationCode, true);
		if (rst.getInt(ChufangConsts.RC) == ChufangConsts.OK) {
			// 获得HttpSession对象
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(5 * 60);
			// 将验证码保存在session对象中,key为validation_code
			session.setAttribute(ChufangConsts.VCODE_KEY2, validationCode.toString());
//			CookieUtil.makeCookie(ChufangConsts.VCODE_KEY2, new AES().aesEncrypt(validationCode.toString(), null), false, "", 5*60);
		}
		return wrapResult(rst);
	}
	
	@RequestMapping(value = "/scan", method = RequestMethod.POST)
	@NoAuth
	public @ResponseBody JSONObject sca(HttpServletRequest request, HttpServletResponse response) {
		
		return wrapResult(ossService.upload(request));
	}

	@RequestMapping(value = "/lstaddress/get", method = RequestMethod.POST)
	public @ResponseBody JSONObject lstAddressGet(HttpServletRequest request, HttpServletResponse response, 
			String shoujihm, String xingming) {
		
		return wrapResult(tiaojicfService.queryLatestAddress(shoujihm, xingming));
	}
	
	private Color getRandomColor(int minColor, int maxColor) {
		Random random = new Random();
		if (minColor > 255) {
			minColor = 255;
		}
		if (maxColor > 255) {
			maxColor = 255;
		}
		// 获得r的随机颜色值
		int red = minColor + random.nextInt(maxColor - minColor);
		int green = minColor + random.nextInt(maxColor - minColor);
		int blue = minColor + random.nextInt(maxColor - minColor);
		return new Color(red, green, blue);
	}
}
