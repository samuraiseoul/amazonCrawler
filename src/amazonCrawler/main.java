package amazonCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class main {
	public static void main(String[] args) throws IOException{
		String URL = "http://www.amazon.com/Tide-Laundry-Detergent-Spring-Meadow/product-reviews/B004YHKVCM/";
		//Gets Page
		Document doc = Jsoup.connect(URL).get();
		
		//Gets highest review page number
		Element paging = doc.getElementsByClass("paging").first();
		Elements pages = paging.getElementsByTag("a");
		pages.remove(pages.last());		
		int numPages = Integer.parseInt(pages.last().html());
		
		String pageURL = URL+"?pageNumber=";
		
		List<Review> reviews = new ArrayList<Review>();
		
		DB db = new DB();
		
		for(int i = 1 ; i <= numPages; i++){
			Document page = Jsoup.connect(pageURL+i).get();
			Element htmlReviews = page.getElementById("productReviews");
			Elements children = htmlReviews.select("td > div");
			Iterator<Element> iter = children.iterator();
			while(iter.hasNext()){
				Review review = new Review();
//				System.out.println("START!"+System.getProperty("line.separator"));
				
				Element currentDiv = iter.next();
				if( !currentDiv.attr("style").equals("margin-left: 5px;")){
					Elements spans = currentDiv.getElementsByTag("span");
					if(spans.size() > 0){
						String stars = spans.get(2).html();
						stars = stars.substring(0, stars.indexOf( ' ' ));
	//					System.out.println("Review given: "+stars);
						review.setStars(stars);
		
						String headline = spans.get(3).getElementsByTag("b").first().html();
	//					System.out.println("HL: "+headline);
						review.setHeadline(headline);
						
						String date = spans.get(3).getElementsByTag("nobr").first().html();
	//					System.out.println("Date: "+date);
						review.setDate(date);
					}
					
					Elements hrefs = currentDiv.getElementsByTag("a");
					if(hrefs.size() > 0 ){
						String userId = hrefs.first().attr("href");
	//					System.out.println("UserId: "+userId.substring(userId.lastIndexOf('/')+1));
						userId = userId.substring(userId.lastIndexOf('/')+1);
						review.setUserId(userId);
					}
					String reviewText = currentDiv.getElementsByClass("reviewText").first().html();
	//				System.out.println("Review: "+review);
					review.setReview(reviewText);
					
					db.addReview(review);
				}else{
					iter.next();
				}
//				System.out.println(System.getProperty("line.separator")+"STOP"+System.getProperty("line.separator"));
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		
//		Element htmlReviews = doc.getElementById("productReviews");
//		Elements children = htmlReviews.select("td > div");
//		Iterator<Element> iter = children.iterator();
//		while(iter.hasNext()){
//			Review review = new Review();
////			System.out.println("START!"+System.getProperty("line.separator"));
//			
//			Element currentDiv = iter.next();
//			
//			Elements spans = currentDiv.getElementsByTag("span");
//			if(spans.size() > 0){
//				String stars = spans.get(2).html();
//				stars = stars.substring(0, stars.indexOf( ' ' ));
////				System.out.println("Review given: "+stars);
//				review.setStars(stars);
//				
//				String headline = spans.get(3).getElementsByTag("b").first().html();
////				System.out.println("HL: "+headline);
//				review.setHeadline(headline);
//				
//				String date = spans.get(3).getElementsByTag("nobr").first().html();
////				System.out.println("Date: "+date);
//				review.setDate(date);
//			}
//			
//			Elements hrefs = currentDiv.getElementsByTag("a");
//			if(hrefs.size() > 0 ){
//				String userId = hrefs.first().attr("href");
//				userId = userId.substring(userId.lastIndexOf('/')+1);
////				System.out.println("UserId: "+userId);
//				review.setUserId(userId);
//			}
//			
//			String reviewText = currentDiv.getElementsByClass("reviewText").first().html();
////			System.out.println("Review: "+reviewText);
//			review.setReview(reviewText);
//			
//			db.addReview(review);
////			System.out.println(review.toString());
////			System.out.println(System.getProperty("line.separator")+"STOP"+System.getProperty("line.separator"));
//		}
		
		
		
		
		
	}
}
