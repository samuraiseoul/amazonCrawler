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

		int numPages = getNumPages(URL);
		
		//Construct URL for crawling all reviews of a product.
		String pageURL = URL+"?pageNumber=";
		
		//Create DB connection.
		DB db = new DB();
		
		for(int i = 1 ; i <= numPages; i++){

			Elements children = getReviews(pageURL, i);
			
			//Iterate over each review
			Iterator<Element> iter = children.iterator();
			
			while(iter.hasNext()){
				Review review = new Review();
				
				Element currentDiv = iter.next();
				
				//Annoying bug fix for when a manufacuter comments on a review.
				if( !currentDiv.attr("style").equals("margin-left: 5px;")){
					
					//Get info from the spans.
					Elements spans = currentDiv.getElementsByTag("span");
					parseSpansIntoReview(spans, review);
					
					//Get info from a tags.
					Elements hrefs = currentDiv.getElementsByTag("a");
					parseLinksIntoReview(hrefs, review);
					
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

	private static int getNumPages(String URL) throws IOException{		
		//Gets Page
		Document doc = Jsoup.connect(URL).get();
		
		//Gets highest review page number
		Element paging = doc.getElementsByClass("paging").first();
		Elements pages = paging.getElementsByTag("a");
		pages.remove(pages.last());		
		return Integer.parseInt(pages.last().html());
	}
	
	private static Elements getReviews(String URL, int i) throws IOException{
		
		Document page = Jsoup.connect(URL+i).get();
		
		//get only the reviews.
		Element htmlReviews = page.getElementById("productReviews");
		return htmlReviews.select("td > div");
	}
	
	private static void parseSpansIntoReview(Elements spans, Review review){
		if(spans.size() > 0){
			//Stars are the second span
			String stars = spans.get(2).html();
			//Part of a bigger string
			stars = stars.substring(0, stars.indexOf( ' ' ));
			review.setStars(stars);

			//The headline and Date are both in the 3rd span
			//headline is wrapped in a b tag
			String headline = spans.get(3).getElementsByTag("b").first().html();
			review.setHeadline(headline);
			
			//date in a nobr tag
			String date = spans.get(3).getElementsByTag("nobr").first().html();
			review.setDate(date);
		}
	}
	
	private static void parseLinksIntoReview(Elements hrefs, Review review){
		if(hrefs.size() > 0 ){
			//userid is in the href of the first <a> tag in each review
			String userId = hrefs.first().attr("href");
			
			//cut away all the ugly parts.
			userId = userId.substring(userId.lastIndexOf('/')+1);
			review.setUserId(userId);
		}		
	}
	
}








