package com.chufang.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectResult;
import com.chufang.consts.ChufangConsts;
import com.chufang.consts.ErrorCode;
import com.chufang.service.OSSService;
import com.chufang.util.StringUtil;

import jxl.format.Orientation;
import net.sf.json.JSONObject;

@Service
public class OSSServiceImpl extends CommonService implements OSSService {

	private final static Logger logger = LoggerFactory.getLogger(OSSServiceImpl.class);
	
	@Value("${oss.endpoint}")
	private String endpoint;
	
	@Value("${oss.accessKeyId}")
	private String accessKeyId;
	
	@Value("${oss.accessKeySecret}")
	private String accessKeySecret;
	
	@Value("${bucket.name}")
	private String bucketName;
	
	private String path = null;
	
	
	@Value("${oss.tmp.access.time}")
	private long tmpAccessTime;

	@Override
	public JSONObject upload(HttpServletRequest request) {
		JSONObject rst = generateRst();
		
	    if (path == null) {
			File uploadDir = new File(request.getSession().getServletContext().getRealPath("/"), ".");
			path = uploadDir.getAbsolutePath();
			
	        if (!uploadDir.exists()) {
	            uploadDir.mkdirs();
	        }
		}
		 //获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(path));
        //设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
        factory.setSizeThreshold(1024*1024) ;
        ServletFileUpload upload = new ServletFileUpload(factory);
        InputStream in = null;
        String objName = null, orginName = null;
        try {
            //可以上传多个文件
            @SuppressWarnings("unchecked")
			List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list) {
        		if (item.isFormField()) {
        			continue;
        		}
        		in = item.getInputStream();
        		objName = item.getName();
//        		orginName =  item.getName();
//        		objName = UUID.randomUUID().toString().replaceAll("-", "") + orginName.substring(orginName.lastIndexOf("."));
        		break;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	rst.put(ChufangConsts.RC, ErrorCode.SCAN_FAIL);
        	return rst;
        }
	    
        ClientConfiguration conf = new ClientConfiguration();
	    // 开启支持CNAME选项
	    conf.setSupportCname(true);
        
	    try {
		    // 创建OSSClient实例
		    OSSClient client = new OSSClient(String.format(endpoint,""), accessKeyId, accessKeySecret);
		    PutObjectResult putRst = client.putObject(bucketName,objName, in);
		    logger.info("server crc=" + putRst.getServerCRC());
		    client.setObjectAcl(bucketName, objName, CannedAccessControlList.PublicRead);
		    rst.put("ulr", String.format(endpoint,bucketName + ".") + "/" +  objName);
	    } catch (Exception e) {
	    	logger.error("fail to send oss file. fileName=" + orginName, e);
	    	rst.put(ChufangConsts.RC, ChufangConsts.FAIL);
	    }
	    return rst;
	}
	
	@Override
	public String getPrivateAccessURL(String objectKey) {
		 String tempKey = null;
		 if (objectKey.startsWith("http")) {
			 tempKey = objectKey.substring(objectKey.lastIndexOf("/") + 1);
		 } else {
			 tempKey = objectKey; 
		 }
		 try {
			 
			 OSSClient client = new OSSClient(String.format(endpoint,""), accessKeyId, accessKeySecret);
			 Date expiration = new Date(new Date().getTime() + 1000L*tmpAccessTime);
			    // 生成URL
			 URL url = client.generatePresignedUrl(bucketName, tempKey, expiration);
			 return url.toString();
		 } catch (Exception e) {
			 return String.format(endpoint,bucketName + ".") + "/" +  tempKey;
		 }
	}
	
//	private byte[] toByteArray(String filename) throws IOException {  
//		  
//        FileChannel fc = null;
//        RandomAccessFile raFile = null;
//        try {  
//        	raFile = new RandomAccessFile(filename, "r");
//            fc = raFile.getChannel();  
//            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,  
//                    fc.size()).load();  
//            byte[] result = new byte[(int) fc.size()];  
//            if (byteBuffer.remaining() > 0) {  
//                byteBuffer.get(result, 0, byteBuffer.remaining());  
//            }  
//            return result;  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//            throw e;  
//        } finally {  
//        	 try {  
//                 raFile.close();  
//             } catch (IOException e) {  
//                 e.printStackTrace();  
//             }  
//        	
//            try {  
//                fc.close();  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }  
//            
//        }  
//    } 

}
