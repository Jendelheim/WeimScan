import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainProgram {
	static MainProgram program = new MainProgram();
	ArrayList<Match> matches = new ArrayList<>(); 
	ArrayList<Match> live_matches = new ArrayList<>(); 
	

	

//	private enum Type {
//		STRING,
//		INT,
//		DOUBLE
//	}

	public static void main(String[] args) throws IOException {

//		 Timer timer = new Timer();
//		timer.schedule(new Receiver(), 0, 6000);

		program.totalCorner();
		program.returnFirstLiveMatch(); 
		
	}


	
	static class  Receiver extends TimerTask {


		ArrayList<String> receivedArrayList = new ArrayList<String>(); 
		
		@Override
		public void run() {
			System.out.println("Hello world - begins");
			try {
				totalCorner();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			System.out.println(receivedArrayList);
			
			System.out.println("hello world - ends");
			
		}
		
		public void totalCorner() throws IOException {
			Document document = Jsoup.connect("http://www.totalcorner.com/match/today").get();
			receivedArrayList = scrapeMatchlist(document, "#inplay_match_table tbody:last-of-type td");
		//	loop(matches);
		//	liveMatch();
		}
		
		private ArrayList<String> scrapeMatchlist(Document document, String selector) {
			Elements elements = document.select(selector);
			ArrayList<String> values = new ArrayList<>();
			
			for (Element element: elements) {
				//System.out.println("1");
				String str = element.text();
			//System.out.println(str);
				values.add(str);
			}
		
			//createMatchObjects(values);
			return values; 
		}
		
	}
	
	
	public void totalCorner() throws IOException {
		Document document = Jsoup.connect("http://www.totalcorner.com/match/today").get();
		scrapeMatchlist(document, "#inplay_match_table tbody:last-of-type td");
		loop(matches);
		liveMatch();
	}
	
	private ArrayList<String> scrapeMatchlist(Document document, String selector) {
		Elements elements = document.select(selector);
		ArrayList<String> values = new ArrayList<>();
		
		for (Element element: elements) {
			String str = element.text();
			values.add(str);
		}
	
		createMatchObjects(values);
		return values; 
	}
	

	
    public void createMatchObjects(ArrayList<String> values){
     	final int NUM_PARAM = 14;
        if(values.size() % NUM_PARAM != 0) {
            System.out.println("Expected values to be divisible by 14. They are " + values.size() + " instead");
        }
       // final int LEAGUE = 0, START_TIME = 1, MATCH_STATUS_MINUTES = 2, MATCH_HOME = 3, SCORE = 4, MATCH_AWAY = 5, MATCH_HANDICAP = 6, CORNER = 7, CORNER_LINE = 8, TOTAL_GOALS = 9, MATCH_ATTACH = 10, MATCH_SHOOT = 11, LIVE_EVENTS = 12, COL = 13;
        int totMatches = values.size()/NUM_PARAM;
        for(int i = 0; i < totMatches; i++) {
                String[] params = new String[NUM_PARAM];
                for(int j = 0; j < NUM_PARAM; j++){
                    params[j] = values.get(i*NUM_PARAM + j);
                }
                Match match = new Match(params); 
                matches.add(match);
            }
        }
    

    public ArrayList<Match> liveMatch(){
    	System.out.println("Hello world");
    	for(Match m : matches){
    		System.out.println("hehheh");
    		if(m.getMinutes() > 0){
    			System.out.println("hahah");
    			m.assignProperties();
    			live_matches.add(m); 
    	//	m.assignProperties();
    		System.out.println(m.getMinutes());
    		}
    	}
    	print_live_matches();
    	return live_matches; 
    }
    

    public Match returnFirstLiveMatch(){
    	Match match; 
    	match = live_matches.get(0); 
    	System.out.println("HEHEHE");
    	System.out.println(match);

    	System.out.println("HEHEHE");
    	

//    	match.setGraphData(92, 2, 1);
//    	match.setGraphData(93, 2, 2);
//    	match.setGraphData(93, 3, 3);
//    	match.setGraphData(94, 3, 4);
//    	match.setGraphData(95, 4, 5);
//    	match.setGraphData(95, 5, 3);
//    	match.setGraphData(96, 6, 5);
//    	match.setGraphData(96, 6, 6);
//    	match.setGraphData(96, 6, 7);
//    	match.setGraphData(96, 7, 8);
    	
//    	match.printData();
    	
    	
    	return match; 
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void print_live_matches(){
    	for(Match m : live_matches){
    		System.out.println(m);
    	}
    }
    
    
	public void loop(ArrayList<Match> matches){
		for(Match match : matches){
			System.out.println(match);
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
