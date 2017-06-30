import org.omg.Messaging.SyncScopeHelper;

public class Match {

	// Tilldela alla v�rden (kanske endast de som kan vara null i senare skede
	// till -1
	String league; // Param 1
	String startTime; // Param 2

	String match_status_minutes; // Param 3
	int minutes;
	boolean started;

	// Hur parsear man s� man kontrollerar om det �r ett gult eller r�tt kort i
	// str�ngen?
	String match_home; // Param 4
//	String hometeam;
	int yellow_card_home;
	int red_card_home;

	String score; // Param 5
	int home_score;
	int away_score;

	String match_away; // Param 6
//	String awayteam;
	int yellow_card_away;
	int red_card_away;

	String match_handicap; // Param 7
	double current_handicap;
	double onbefore_handicap;

	String corner; // Param 8
	String corner_line; // Param 9

	String total_goals; // Param 10
	double current_goal_line;
	double onbefore_goal_line;
	double current_goal_line_half;
	double onbefore_goal_line_half;

	String match_attach; // Param 11
	int home_attach;
	int away_attach;

	String match_shoot; // Param 12
	int home_shoot;
	int away_shoot;

	String live_events; // Param 13
	String COL; // Param 14

	String c_analysis;
	String o_analysis;
	String l_analysis;

	int[][] graph_data = new int[6][120];
	static int SCORE_HOME = 0, SCORE_AWAY = 1, DANGEROUS_ATTACKS_HOME = 2, DANGEROUS_ATTACKS_AWAY = 3, SHOTS_HOME = 4,
			SHOTS_AWAY = 5;

	public Match(String league, String startTime, String match_status_minutes, String match_home, String score,
			String match_away, String match_handicap, String corner, String corner_line, String total_goals,
			String match_attach, String match_shoot, String live_events, String COL) {
		this.league = league;
		this.startTime = startTime;
		this.match_status_minutes = match_status_minutes;
		this.match_home = match_home;
		this.score = score;
		this.match_away = match_away;
		this.match_handicap = match_handicap;
		this.corner = corner;
		this.corner_line = corner_line;
		this.total_goals = total_goals;
		this.match_attach = match_attach;
		this.match_shoot = match_shoot;
		this.live_events = live_events;
		this.COL = COL;
	}

	public Match(String[] params) {
		this.league = params[0];
		this.startTime = params[1];
		this.match_status_minutes = params[2];
		this.match_home = params[3];
		this.score = params[4];
		this.match_away = params[5];
		this.match_handicap = params[6];
		this.corner = params[7];
		this.corner_line = params[8];
		this.total_goals = params[9];
		this.match_attach = params[10];
		this.match_shoot = params[11];
		this.live_events = params[12];
		this.COL = params[13];

		if (match_status_minutes.matches("\\d+")) {
			this.minutes = Integer.parseInt(match_status_minutes);
		}

	}

	// �r det rimligt att g�ra en metod som denna f�r att inte beh�va assigna
	// alla v�rden i on�dan utan endast dem man testar?
	
	//M�ste kunna uppdatera samma objekt utan att skapa ett nytt, passa med v�rden i assignProp s� man kan �ndra om. 
	
	public void assignProperties(String match_status_minutes, String score, String match_handicap, String total_goals, String match_attach, String match_shoot){ 

		if (match_status_minutes.matches("\\d+")) {
			minutes = Integer.parseInt(match_status_minutes);
		}

		String[] goals = score.split(" - ");
		home_score = Integer.parseInt(goals[0]);
		away_score = Integer.parseInt(goals[1]);
		
		System.out.println("home_score: " + home_score);
		System.out.println("away_score: " + away_score);
//		
//		System.out.println("testing match_handicap: " + match_handicap);
		String[] handicaps = match_handicap.split(" ");
		
		if(!match_handicap.isEmpty()){
			current_handicap = Double.parseDouble(handicaps[0]);
			
			if(handicaps.length > 1){
				onbefore_handicap = Double.parseDouble(handicaps[1].replaceAll("[^\\d.]", ""));
			}
			
		}

		String[] goal_lines = total_goals.split(" ");
		
		
		if(!total_goals.isEmpty()){
			current_goal_line = Double.parseDouble(goal_lines[0]);
			
			if(goal_lines.length > 1){
				onbefore_goal_line = Double.parseDouble(goal_lines[1].replaceAll("[^\\d.]", ""));
			}
			
			 if (goal_lines.length == 4) {
			 current_goal_line_half = Double.parseDouble(goal_lines[2]);
			 onbefore_goal_line_half = Double.parseDouble(goal_lines[3].replaceAll("[^\\d.]", ""));
			 }
		}


//		 SKA KOMMENTERAS UPP N�R DE �R MATCHER LIVE, KNASAR ANNARS 
		
		
		try{
			 System.out.println("Match-attach !!! - " + match_attach);
				String[] attachs = match_attach.split(" - "); 
				home_attach = Integer.parseInt(attachs[0]); 
				away_attach = Integer.parseInt(attachs[1]); 
		}
		catch (final NumberFormatException ex){
			home_attach = 0; 
			away_attach = 0; 
		}
		
		try{
			 System.out.println("Match-shoot !!! - " + match_shoot);
				String[] shoots = match_shoot.split(" - "); 
				home_shoot = Integer.parseInt(shoots[0]); 
				away_shoot = Integer.parseInt(shoots[1]); 
		}
		catch (final NumberFormatException ex){
			home_attach = 0; 
			away_attach = 0; 
		}


		
		
	}
	
	
	

	public int getMinutes() {
		return minutes;
	}
	
	public int getHomeScore(){
		return home_score; 
	}
	
	public int getAwayScore(){
		return away_score; 
	}
	
	public int getHomeAttach(){
		return home_attach; 
	}
	
	public int getAwayAttach(){
		return away_attach; 
	}
	
	public int getHomeShots(){
		return home_shoot; 
	}
	
	public int getAwayShots(){
		return away_shoot; 
	}
	

	
	public String getHomeTeam(){
		return match_home; 
	}
	
	public String getAwayTeam(){
		return match_away;
	}
	
	public String getScore(){
		return score; 
	}
	
	public String getMinuteString(){
		return match_status_minutes; 
	}
	
	public String getMatchHandicap(){
		return match_handicap; 
	}
	
	public String getTotalGoals(){
		return total_goals; 
	}
	
	public String getMatchAttach(){
		return match_attach; 
	}
	
	public String getMatchShoot(){
		return match_shoot; 
	}
	
	
	

	public void setGraphData(int minute, int home_score, int away_score, int home_attach, int away_attach, int home_shots, int away_shots) {

//		System.out.println(this.graph_data[SCORE_HOME][minute]);
//		System.out.println(this.graph_data[SCORE_AWAY][minute]);
		
//		System.out.println("A1: " + minute);
//		System.out.println("A2: " + home_score);
//		System.out.println("A3: " + away_score);
//		System.out.println("A4: " + home_attach);
//		System.out.println("A5: " + away_attach);
//		System.out.println("A6: " + home_shots);
//		System.out.println("A7: " + away_shots);
		

		if (this.graph_data[SCORE_HOME][minute] < home_score) {
			this.graph_data[SCORE_HOME][minute] = home_score;
		}
		if (this.graph_data[SCORE_AWAY][minute] < away_score) {
			this.graph_data[SCORE_AWAY][minute] = away_score;
		}
		if (this.graph_data[DANGEROUS_ATTACKS_HOME][minute] < home_attach) {
			this.graph_data[DANGEROUS_ATTACKS_HOME][minute] = home_attach;
		}
		if (this.graph_data[DANGEROUS_ATTACKS_AWAY][minute] < away_attach) {
			this.graph_data[DANGEROUS_ATTACKS_AWAY][minute] = away_attach;
		}
		if (this.graph_data[SHOTS_HOME][minute] < home_shots) {
			this.graph_data[SHOTS_HOME][minute] = home_shots;
		}
		if (this.graph_data[SHOTS_AWAY][minute] < away_shots) {
			this.graph_data[SHOTS_AWAY][minute] = away_shots;
		}

		
//		System.out.println("TESTETETETET");
//		System.out.println(this.graph_data[SCORE_HOME][minute]);
//		System.out.println(this.graph_data[SCORE_AWAY][minute]);

	}
	
	public void printGraphData(){
		for(int i = 0; i < 6; i++){
			System.out.print(i + " ");
			for(int j = 0; j < 120; j++){
				System.out.print(graph_data[i][j]); // println skriver ut alla index f�r sig s� man kan se r�tt tilldelning. 
			}
			System.out.println("SPLITTER");
		}
	}

	public void printData(int minute) {
		
		
//		System.out.println(this.graph_data[SCORE_HOME][minute -2]);
//		System.out.println(this.graph_data[SCORE_HOME][minute -1]);
//		System.out.println(this.graph_data[SCORE_HOME][minute]);
//		System.out.println(this.graph_data[SCORE_HOME][minute +1]);
//		System.out.println(this.graph_data[SCORE_HOME][minute +2]);
//
//		System.out.println("HEHEHE");
//
////		System.out.println(this.graph_data[SCORE_AWAY][minute -2]);
////		System.out.println(this.graph_data[SCORE_AWAY][minute -1]);
//		System.out.println(this.graph_data[SCORE_AWAY][minute]);
//		System.out.println(this.graph_data[SCORE_AWAY][minute +1]);
//		System.out.println(this.graph_data[SCORE_AWAY][minute +2]);

	}

	
	public String toString() {
		return "Match: " + league + " --- " + startTime + " ---:" + match_status_minutes + ":--- " + match_home + " --- "
				+ score + " --- " + match_away + " --- " + match_handicap + " --- " + corner + " --- " + corner_line
				+ " --- " + total_goals + " --- " + match_attach + " --- " + match_shoot + " --- " + live_events
				+ " --- " + COL;
	}
	
	
}
