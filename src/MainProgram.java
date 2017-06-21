import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainProgram {
	static MainProgram program = new MainProgram();
	ArrayList<Match> matches = new ArrayList<>(); 

	private enum Type {
		STRING,
		INT,
		DOUBLE
	}

	public static void main(String[] args) throws IOException {
		//program.wikipediaNews();

		program.totalCorner();


	}

//	public void wikipediaNews() throws IOException {
//
//		Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
//		Elements newsHeadlines = doc.select("#mp-itn b a");
//
//		System.out.println(newsHeadlines);
//
//	}

	public void totalCorner() throws IOException {
		Document document = Jsoup.connect("http://www.totalcorner.com/match/today").get();
		scrapeMatchlist(document, Type.STRING, "#inplay_match_table tbody:last-of-type td");
		loop(matches);
	
	}
	
	private ArrayList<String> scrapeMatchlist(Document document, Type type, String selector) {
		Elements elements = document.select(selector);
		ArrayList<String> values = new ArrayList<>();
		
		for (Element element: elements) {
			String str = element.text();
			values.add(str);
		}
	
		createMatchObjects(values);
		return values; 
	}
	
	public void loop(ArrayList<Match> matches){
		for(Match match : matches){
			System.out.println(match);
		}
	}
	
    public void createMatchObjects(ArrayList<String> values){
     	final int NUM_PARAM = 14;
        if(values.size() % NUM_PARAM != 0) {
            System.out.println("Expected values to be divisible by 14. They are " + values.size() + " instead");
        }
        final int LEAGUE = 0, START_TIME = 1, MATCH_STATUS_MINUTES = 2, MATCH_HOME = 3, SCORE = 4, MATCH_AWAY = 5, MATCH_HANDICAP = 6, CORNER = 7, CORNER_LINE = 8, TOTAL_GOALS = 9, MATCH_ATTACH = 10, MATCH_SHOOT = 11, LIVE_EVENTS = 12, COL = 13;
        int totMatches = values.size()/NUM_PARAM;
        for(int i = 0; i < totMatches; i++) {
                String[] params = new String[NUM_PARAM];
                for(int j = 0; j < NUM_PARAM; j++){
                    params[j] = values.get(i*NUM_PARAM + j);
                }
                Match match = new Match(params); // change this to accept an array of String
                // you can now refer to the values as param[ThisClassName.LEAGUE]
                matches.add(match);
            }
        }
    


//	
//	public void createMatchObjects(ArrayList<String> values){
//		String league = null; 
//		String startTime = null; 
//		String match_status_minutes = null; 
//		String match_home = null; 
//		String score = null; 
//		String match_away = null; 
//		String match_handicap = null; 
//		String corner = null; 
//		String corner_line = null; 
//		String total_goals = null; 
//		String match_attach = null; 
//		String match_shoot = null; 
//		String live_events = null; 
//		String COL = null; 
//		
//		for(int i = 0; i < values.size(); i++){
//			if(i % 14 == 0){
//				league = values.get(i); 
//			}
//			else if(i % 14 == 1){
//				startTime = values.get(i); 
//			}
//			else if(i % 14 == 2){
//				match_status_minutes = values.get(i); 
//			}
//			else if(i % 14 == 3){
//				match_home = values.get(i); 
//			}
//			else if(i % 14 == 4){
//				score = values.get(i); 
//			}
//			else if(i % 14 == 5){
//				match_away= values.get(i); 
//			}
//			else if(i % 14 == 6){
//				match_handicap = values.get(i); 
//			}
//			else if(i % 14 == 7){
//				corner = values.get(i); 
//			}
//			else if(i % 14 == 8){
//				corner_line = values.get(i); 
//			}
//			else if(i % 14 == 9){
//				total_goals= values.get(i); 
//			}
//			else if(i % 14 == 10){
//				match_attach = values.get(i); 
//			}
//			else if(i % 14 == 11){
//				match_shoot = values.get(i); 
//			}
//			else if(i % 14 == 12){
//				live_events = values.get(i); 
//			}
//			else if(i % 14 == 13){
//				COL = values.get(i); 
//				Match match = new Match(league, startTime, match_status_minutes, match_home, score, match_away, match_handicap, corner, corner_line, total_goals, match_attach, match_shoot, live_events, COL); 
//				matches.add(match); 
//			}
//		}
//		
//		
//		
//		
//	}
	
//	private <T> ArrayList<T> getElementsFor(Document document, Type type, String selector) {
//		Elements elements = document.select(selector);
//		ArrayList<T> values = parseElements(elements, Type.STRING);
//		
//		for (Element element: elements) {
//			switch (type) {
//			case STRING:
//				String str = element.text();
//				values.add(str);
//			}
//		}
//		
//		return values;
//	}
//	
//	private <T> ArrayList<T> parseElements(Elements elements, Type type) {
//		
//	}
}
