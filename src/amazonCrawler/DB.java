package amazonCrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

 
public class DB {
 
	public Connection conn = null;
 
	public DB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/crawler";
			conn = DriverManager.getConnection(url, "root", "");
			System.out.println("conn built");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
		
	public void addReview(Review review){
		String query = "INSERT INTO reviews (review, stars, headline, date, user_id, product_id) VALUES (?,?,?,?,?,?)";
		System.out.println(query);
		
		PreparedStatement prepState = null;
		
		try{
			prepState = conn.prepareStatement(query);
			prepState.setString(1, review.getReview());
			prepState.setFloat(2, review.getStars());
			prepState.setString(3, review.getHeadline());
			prepState.setDate(4, new java.sql.Date( review.getDate().getTime() ));
			prepState.setString(5, review.getUserId());
			prepState.setString(6, review.getProductId());
			prepState.executeUpdate();
		}catch(Exception e){
			System.out.println(review.getUserId());
			e.printStackTrace();
			System.exit(0);
		}
	}
 
	@Override
	protected void finalize() throws Throwable {
		if (conn != null || !conn.isClosed()) {
			conn.close();
		}
	}
}

