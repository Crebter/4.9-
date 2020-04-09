package com.sxt.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HomeWork {
	
	 public static void main(String[] s) throws IOException {
		 boolean flag = true;
		 while(flag) {
			 	judge();
		    	System.out.println("###是否要查看修改的历史记录###");
		    	System.out.println("1.确定      2.否定     3.退出");
		    	Scanner sr = new Scanner(System.in);
		    	int choice = sr.nextInt();
		    	switch(choice) {
		    		case 1:
		    			showAll();
		    			break;
		    		case 2:
		    			flag = true;
		    			break;
		    		case 3:
		    			return;
		    		default:
		    			System.out.println("你的输入有误!");
		    			break;
		    			
		    	}
		 }
	    	
	    }
	
	
	 public static void replaceTxtByLineNo(String path,int lineNo,String newStr) {
	        String temp = "";
	        boolean flag1 = false;
	        try {
	            File file = new File(path);
	            FileInputStream fis = new FileInputStream(file);
	            InputStreamReader isr = new InputStreamReader(fis,"utf-8");//字节流转成字符流
	            BufferedReader br = new BufferedReader(isr);//对字符流进行修饰，提高效率
	            StringBuffer buf = new StringBuffer();

	            // 除了要修改的那行，其他行数的内容不变
	            for (int j = 1; (temp = br.readLine()) != null; j++) {
	                if(j==lineNo){
	                    buf = buf.append(newStr);
	                }else{
	                    buf = buf.append(temp);
	                }
	                buf = buf.append(System.getProperty("line.separator"));//每一行都添上换行符
	            }
	            flag1 = true;
	            br.close();
	            FileOutputStream fos = new FileOutputStream(file);
	            PrintWriter pw = new PrintWriter(fos);
	            pw.write(buf.toString().toCharArray());//将修改后的内容写入文件or缓冲区
	            pw.flush();
	            pw.close();
	            if(flag1) {
	            	System.out.println("操作成功！");
	            }else {
	            	System.out.println("操作失败!");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 	public static void judge() throws IOException {
	 		System.out.print("请输入你的路径：");
	    	Scanner sr = new Scanner(System.in);
	    	String path = sr.nextLine();
	    	File file = new File(path);
	    	boolean flag = file.createNewFile();
	    	if(flag) {
	    		System.out.print("该路径下没有该文件，已为你自动创建!");
	    	}else {
	    		System.out.print("该路径下有该文件，请输入你要修改第几行：");
		    	int lineNumber = sr.nextInt();
		    	System.out.print("请输入你修改后的内容:");
		    	Scanner sr1 = new Scanner(System.in);
		    	String newstr = sr1.nextLine();
		    	insert(newstr); //插入数据库
		    	replaceTxtByLineNo(path,lineNumber,newstr);
	    	}
	 	
	 	
	 	}
	 	
	   
	    
	    //使用JDBC连接Mysql数据库
	    public static void insert(String newstr) {
	    	Connection con = null;
	    	PreparedStatement stmt = null;
	    	
	    	try {
	    		//取得与数据库的连接
		    	con = JDBCUtils.getConnection();
		    	//输入插入sql语句
		    	String sql = "insert into pre_operation(content) values(?) ";
		    	//向数据库发起请求
				stmt = con.prepareStatement(sql);
				stmt.setString(1,newstr);
				//执行语句
				stmt.executeUpdate();
				System.out.println("插入成功!");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				JDBCUtils.close(stmt,con);
			}
	    	
	    	
	    	
	    	
	    }
	    
	    public static void showAll() {
	    	Connection con = null;
	    	PreparedStatement stmt = null;
	    	ResultSet rs = null;
	    	con = JDBCUtils.getConnection();
	    	
	    	
	    	try {
	    		String sql = "select * from pre_operation";
				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();
				while(rs.next()) {
					System.out.println("ID："+rs.getInt(1)+","+"修改的内容"+rs.getString(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				JDBCUtils.close(rs, stmt, con);
			}
	    }
	    
}
