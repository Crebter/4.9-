package com.sxt.io;
//工具类一般会把里面的方法改为静态方法，可以通过类名调用方法
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
//建立连接的代码
public class JDBCUtils {
	//设置常量便于后期修改用户名及密码
	private static final String connectionUrl = "jdbc:mysql://localhost:3306/qg_homework?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
	private static final String username = "root";
	private static final String password = "wudonglong";
	
	private static ArrayList<Connection> conList = new ArrayList<Connection>();//创建一个集合，放置Connection，即连接池
	
	//建立多个Connection，便于后期调用
	static {
		for(int i=0;i<5;i++) {
			Connection con = createConnection();
			conList.add(con);
		}
	}
	
	//获得连接
	public static  Connection getConnection() {
		if(conList.isEmpty() == false) {
			Connection con = conList.get(0);//取得连接
			conList.remove(con);//当有一个人用的时候，其他人就不能用了
			return con;
		}else {
			//当集合里面没有连接的时候，新创建连接来用
			return createConnection();
		}
	}
	
	//创建连接
	private static Connection createConnection() {
		//注册驱动
		//做链接
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(connectionUrl, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	//关闭
	public static void close(ResultSet rs,Statement stmt,Connection con) {
		closeResultSet(rs);
		closeStatement(stmt);
		closeConnection(con);
	}
	
	public static void close(Statement stmt1,Statement stmt2,Connection con) {
		closeStatement(stmt1);
		closeStatement(stmt2);
		closeConnection(con);
	}
	
	public static void close(Statement stmt,Connection con) {
		
		closeStatement(stmt);
		closeConnection(con);
	}
	
	private static void closeResultSet(ResultSet rs) {
		try {
			if(rs!=null)
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void closeStatement(Statement stmt) {
		try {
			if(stmt!=null)
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void closeConnection(Connection con) {
//		try {
//			if(con!=null)
//			con.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		//将Connection放回连接池就等于归还
		conList.add(con);
		}
	
}
