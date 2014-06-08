package amazonCrawler;

import java.util.Date;

public class Review {
	private String review;
	private float stars;
	private String userId;
	private Date date;
	private String headline;
	private String productId;
	
	public void setReview(String rev){
		this.review = rev;
	}
	
	public void setStars(String strs){
		this.stars = Float.parseFloat(strs);
	}

	public void setUserId(String id){
		this.userId = id;
	}
	
	public void setProductId(String id){
		this.productId = id;
	}
	
	public void setDate(String dt){
		this.date = new Date(dt);
	}
	
	public void setHeadline(String hl){
		this.headline = hl;
	}
	
	public String getReview(){
		return this.review;
	}
	
	public String getHeadline(){
		return this.headline;
	}

	public String getUserId(){
		return this.userId;
	}
	
	public String getProductId(){
		return this.productId;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public float getStars(){
		return this.stars;
	}
	
	@Override public String toString(){
		return " Stars: "+this.stars+System.getProperty("line.separator")
				+" User: "+this.userId+System.getProperty("line.separator")
				+" Date: "+this.date.toString()+System.getProperty("line.separator")
				+" Headline: "+this.headline+System.getProperty("line.separator")
				+"Review: "+this.review+System.getProperty("line.separator");
	}
}
