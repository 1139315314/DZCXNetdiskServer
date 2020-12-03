package com.dzcx.netdisk.socket;

import com.dzcx.netdisk.Main;
import com.dzcx.netdisk.api.APIFactory;
import com.dzcx.netdisk.entity.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;

import java.io.*;
import java.net.Socket;

/**
 * 公共请求接口
 * <br />
 * 接收大部分客户端请求内容
 * 
 * @author Yeyu
 *
 */
public class PublicSocket extends Thread {

	private Socket socket;
	private OutputStream os;
	private Config config = Main.config;

	public PublicSocket(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			JsonParser jp = new JsonParser();
			JsonObject jo = (JsonObject) jp.parse(br.readLine());
			JsonElement value = jo.get("value");
			os = socket.getOutputStream();
				switch (jo.get("key").getAsString()) {
					case "getConfig":        // 获取服务器配置
						response(APIFactory.getApi().getConfig());
						break;
					case "setText":          // 保存文本
						// TODO 保存文本
						//APIFactory.getApi().setText(value);
						break;
					case "getText":          // 获取文本
						response(APIFactory.getApi().getText(value));
						break;
					case "getImg":           // 获取图片
						// TODO 获取图片
						Main.log.info("预览图片 -> " + value.getAsString());
						responseImg(value.getAsString());
						break;
					case "list":             // 获取文件列表
						response(APIFactory.getApi().getFileList(value));
						break;
					case "folder":           // 获取文件夹列表
						// TODO 获取文件夹列表
						//response(APIFactory.getApi().getFolderList(value));
						break;
					case "zip":              // 压缩
						try {
							// TODO 压缩
							//APIFactory.getApi().zip(value);
						} catch (Exception e) {
							response("fail");
						}
						break;
					case "unzip":            // 解压
						try {
							// TODO 解压
							//APIFactory.getApi().unZip(value);
						} catch (Exception e) {
							response("fail");
						}
						break;
					case "newFolder":        // 新建文件夹
						//APIFactory.getApi().newFolder(value);
						break;
					case "newText":          // 新建文本文档
						//APIFactory.getApi().newText(value);
						break;
					case "rename":           // 重命名
						//response(APIFactory.getApi().renameFile(value));
						break;
					case "move":             // 移动
						//APIFactory.getApi().moveFiles(value);
						break;
					case "copy":             // 复制
						//APIFactory.getApi().copyFiles(value);
						break;
					case "delete":           // 删除
						value = (JsonElement) jp.parse(value.getAsString());
						JsonArray list = value.getAsJsonArray();
						String path = Main.root;
						String isPublic = list.get(0).getAsString();
						if (config.getPublicFile() != null && isPublic.indexOf(config.getPublicFile()) != -1) path = "";
						for (int i = 0; i < list.size(); i++) {
							//APIFactory.getApi().eachDeleteFiles(new File(path + list.get(i).getAsString()));
							response(i + 1);
						}
						break;
					case "getPhotoDateList": // 获取照片日期
						//response(APIFactory.getApi().getPhotoDateList());
						break;
					case "addYear":          // 新增年份
						//APIFactory.getApi().addYear(value.getAsString());
						break;
					case "getImgPM":         // 照片管理器获取图片缩略图
						responseImgPM(value.getAsString());
						break;
					case "getPhotoInfo":     // 获取照片信息
						//response(APIFactory.getApi().getPhotoInfo(value));
						break;
					case "getMP4Info":       // 获取 MP4 元数据
						//response(APIFactory.getApi().getMP4Info(value));
						break;
				}
			response("finish");
			br.close();
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void response(Object data) throws IOException {
		if (!socket.isClosed() && os != null) {
			os = socket.getOutputStream();
			os.write((data.toString() + "\r\n").getBytes("UTF-8"));
		}
	}
	
	// 照片管理器获取缩略图
	private void responseImgPM(String path) throws Exception {
		if (Boolean.valueOf(config.isCompressImg())) { // 压缩
			response("size1"); // 压缩图片，不需要 size 参数，字符串前有 size 即可
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			if (br.readLine().equals("ready")) {
				os = socket.getOutputStream();
				File file = new File(Main.root + File.separator + config.getPhoto().toString() + File.separator + path);
				FileInputStream isFIS = new FileInputStream(file);
				Main.log.info("照片管理器 -> 压缩照片 -> " + path);
				FileInputStream outFIS = new FileInputStream(file);
				Image img = new Image(isFIS);
				if (img.getWidth() < img.getHeight()) {
					Thumbnails.of(outFIS).width(128).toOutputStream(os);
				} else {
					Thumbnails.of(outFIS).height(128).toOutputStream(os);
				}
				System.gc();
				isFIS.close();
				outFIS.close();
				is.close();
				br.close();
			}
		} else { // 非压缩
			Main.log.info("照片管理器 -> 发送原图 -> " + path);
			responseImg(File.separator + config.getPhoto().toString() + File.separator + path);
		}
	}
	
	// 获取原图
	private void responseImg(String path) throws Exception {
		File file = new File(Main.root + path);
		response("size" + file.length());
		InputStream is = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		if (br.readLine().equals("ready")) {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[4096];
			int l = 0;
			while ((l = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, l);
			}
			fis.close();
			dos.flush();
			dos.close();
			br.close();
		}
	}
}