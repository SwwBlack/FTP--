package view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Ftputil {
	//
	public  static Socket socket = null;
	//
	private BufferedReader reader = null;
	//
	private BufferedWriter writer = null;
/**
 * connectServer
 * 连接ftp服务器
 * @throws Exception 
 * 
 */
	public int connectServer(String server,String user,String pawd,int port) throws Exception{
		//server:服务器地址        user：用户名
		//pawd：密码          port:端口
		if(socket!=null){
			throw new Exception("already connect！");
		}
		socket = new Socket(server, port);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		String response = readLine();  
        if(!response.startsWith("220")) {  
            return 0; 
        }  
        sendLine("USER " + user);  
        response = readLine();  
        if(!response.startsWith("331")) {  
           return 0;  
        }  
        sendLine("PASS " + pawd);  
        response = readLine();  
        if(!response.startsWith("230")) {  
            return 0;  
        }  
        return 1;
	}
	/*
	 * 断开连接
	 * */
	public int  Dukaiconnect(){
		try {
			socket.close();
			if(socket!=null)
				socket=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
	//向服务器发送命令和接收响应
	private void sendLine(String line) throws Exception{
		if(socket == null) {  
            throw new Exception("not connect!");  
        }
		//写命令
        writer.write(line + "\r\n");  
        writer.flush();  
	}
	private String readLine() throws IOException { 
		//读响应
        String line = reader.readLine();  
        return line;  
    }  
	/**
	 * 
	 * 上传文件
	 */ 
    public synchronized boolean stor(File file) throws Exception {  
        //if(!file.isDirectory()) {  
        //    throw new Exception("cannot upload a directory!");  
        //}  
        String fileName = file.getName();  
        return upload(new FileInputStream(file), fileName);  
    }  
      
    @SuppressWarnings("resource")
    //上传文件函数
	public synchronized boolean upload(InputStream inputStream, String fileName) throws Exception {
    	//fileName为文件路径
        BufferedInputStream input = new BufferedInputStream(inputStream);  
        sendLine("PASV"); //发送命令使服务器进入被动模式
        String response = readLine();  //接收响应
        if(!response.startsWith("227")) {  
            throw new Exception("not request passive mode!");  
        }  
        String ip = null;  
        int port = -1;  
        int opening = response.indexOf('(');  
        int closing = response.indexOf(')', opening + 1);  
        if(closing > 0) {  
            String dataLink = response.substring(opening + 1, closing);  
            StringTokenizer tokenzier = new StringTokenizer(dataLink, ",");  
            try {  
                ip = tokenzier.nextToken() + "." + tokenzier.nextToken() + "."   
                        +  tokenzier.nextToken() + "." + tokenzier.nextToken();  
                port = Integer.parseInt(tokenzier.nextToken()) * 256 +Integer.parseInt(tokenzier.nextToken());; 
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                throw new Exception("bad data link after upload!");  
            }  
        } 
        //发送文件上传命令
        sendLine("STOR " + fileName);  
        Socket dataSocket = new Socket(ip, port); 
        //System.out.println(ip+" "+port);
        response = readLine();  
        if(!response.startsWith("150")) {  
            throw new Exception("not allowed to send the file!");  
        }  
        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        //文件写入服务器文件夹
        byte[] buffer = new byte[4096];  
        int bytesRead = 0;  
        while((bytesRead = input.read(buffer)) != -1) {  
            output.write(buffer, 0, bytesRead);  
        }  
        output.flush();  
        output.close();  
        input.close();
        
        response = readLine();
        dataSocket.close(); 
        return response.startsWith("226");  
    } 
    //下载文件
    public int downloadFile(String localPath, String ftpPath) throws Exception {  
        // 进入被动模式  
        sendLine("PASV");  
  
        // 获得ip和端口  
        /*
        String response = readLine();
        String ip = null;  
        int port = -1;  
        int opening = response.indexOf('(');  
        int closing = response.indexOf(')', opening + 1);  
        if(closing > 0) {  
            String dataLink = response.substring(opening + 1, closing);  
            StringTokenizer tokenzier = new StringTokenizer(dataLink, ",");  
            try {  
                ip = tokenzier.nextToken() + "." + tokenzier.nextToken() + "."   
                        +  tokenzier.nextToken() + "." + tokenzier.nextToken();  
                port = Integer.parseInt(tokenzier.nextToken()) * 256 +Integer.parseInt(tokenzier.nextToken())+2;  
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                throw new Exception("bad data link after upload!");  
            }  
        }  
        sendLine("RETR " + ftpPath); 
        Socket dataSocket = new Socket(ip, port);
        System.out.println(ip+" "+port);
        InputStream inputStream = dataSocket.getInputStream();*/
        // 下载文件前的准备  
        File localFile = new File(localPath);  
        FileInputStream inputStream = new FileInputStream(new File(ftpPath));  
        FileOutputStream fileOutputStream = new FileOutputStream(localFile); 
  
        // 下载文件  
        int offset;  
        byte[] bytes = new byte[1024];  
        while ((offset = inputStream.read(bytes)) != -1) {  
            fileOutputStream.write(bytes, 0, offset);  
        }  
        System.out.println("download success!!");  
  
        // 下载文件后的善后工作  
        inputStream.close();  
        fileOutputStream.close();  
        //dataSocket.close(); 
        return 1;
    }  
    /** 
     * 得到目录
     * @return 
     * @throws Exception 
     */  
    public synchronized String pwd() throws Exception {  
        sendLine("PWD");  
        String dir = null;  
        String response = readLine();  
        System.out.println(response);
        if(response.startsWith("257")) {  
            int firstQuote = response.indexOf("/");  
            int secondQuote = response.indexOf("/", firstQuote + 1);  
            if(secondQuote > 0) {  
                dir = response.substring(firstQuote + 1, secondQuote);  
            }  
        }  
        return dir;  
    } 
    public ArrayList<File> showFile(String dir){
    	ArrayList<File> list = new ArrayList<File>();
    	File root = new File(dir);
    	File[] files = root.listFiles();
    	//if(files==null)
    		//System.out.println("haha");
    	//System.out.println(files.length);
       for (File file : files) {
		list.add(file);
	}
		return list;
    	
    }
	
}
