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
		
		//Construct URL for crawling all reviews of a product.
		String pageURL = URL+"?pageNumber=";
		
		//Create DB connection.
		DB db = new DB();
		
		for(int i = 1 ; i <= numPages; i++){
			
			Document page = Jsoup.connect(pageURL+i).get();
			
			//get only the reviews.
			Element htmlReviews = page.getElementById("productReviews");
			Elements children = htmlReviews.select("td > div");
			
			//ITerate over each review
			Iterator<Element> iter = children.iterator();
			while(iter.hasNext()){
				Review review = new Review();
				
				Element currentDiv = iter.next();
				
				//Annoying bug fix for when a manufacuter comments on a review.
				if( !currentDiv.attr("style").equals("margin-left: 5px;")){
					
					//Get info from the spans.
					Elements spans = currentDiv.getElementsByTag("span");
					if(spans.size() > 0){
						String stars = spans.get(2).html();
						stars = stars.substring(0, stars.indexOf( ' ' ));
						review.setStars(stars);
		
						String headline = spans.get(3).getElementsByTag("b").first().html();
						review.setHeadline(headline);
						
						String date = spans.get(3).getElementsByTag("nobr").first().html();
						review.setDate(date);
					}
					
					//Get info from a tags.
					Elements hrefs = currentDiv.getElementsByTag("a");
					if(hrefs.size() > 0 ){
						String userId = hrefs.first().attr("href");
						userId = userId.substring(userId.lastIndexOf('/')+1);
						review.setUserId(userId);
					}
					
					//get the review
					String reviewText = currentDiv.getElementsByClass("reviewText").first().html();
					review.setReview(reviewText);
					
					//add to db
					db.addReview(review);
				}else{
					//skip the manufacturer response
					iter.next();
				}
			}
		}
	}
}
