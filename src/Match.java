
public class Match {

	// Tilldela alla värden (kanske endast de som kan vara null i senare skede
	// till -1
	String league; // Param 1
	String startTime; // Param 2

	String match_status_minutes; // Param 3
	int minutes;
	boolean started;

	// Hur parsear man så man kontrollerar om det är ett gult eller rött kort i
	// strängen?
	String match_home; // Param 4
	String hometeam;
	int yellow_card_home;
	int red_card_home;

	String score; // Param 5
	int home_score;
	int away_score;

	String match_away; // Param 6
	String awayteam;
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

	// Är det rimligt att göra en metod som denna för att inte behöva assigna
	// alla värden i onödan utan endast dem man testar?
	
	//Måste kunna uppdatera samma objekt utan att skapa ett nytt, passa med värden i assignProp så man kan ändra om. 
	//public void assignProperties(String match_status_minutes, String score, String match_handicap, String total_goals, String match_attach, String match_shoot); 
	
	public void assignProperties() {
		

		minutes = Integer.parseInt(match_status_minutes); 

		String[] goals = score.split(" - ");
		home_score = Integer.parseInt(goals[0]);
		away_score = Integer.parseInt(goals[1]);
		
		String[] handicaps = match_handicap.split(" ");
		current_handicap = Double.parseDouble(handicaps[0]);
		onbefore_handicap = Double.parseDouble(handicaps[1]);
		
		String[] goal_lines = total_goals.split(" ");
		
		current_goal_line = Double.parseDouble(goal_lines[0]);
		onbefore_goal_line = Double.parseDouble(goal_lines[1]);
		
		 if (goal_lines.length == 4) {
		 current_goal_line_half = Double.parseDouble(goal_lines[2]);
		 onbefore_goal_line_half = Double.parseDouble(goal_lines[3]);
		 }
		 
		String[] attachs = match_attach.split(" - "); 
		home_attach = Integer.parseInt(attachs[0]); 
		away_attach = Integer.parseInt(attachs[1]); 
		
		String[] shoots = match_shoot.split(" - "); 
		home_shoot = Integer.parseInt(shoots[0]); 
		away_shoot = Integer.parseInt(shoots[1]); 
		
		
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
		return hometeam; 
	}
	
	public String returnAwayTeam(){
		return awayteam;
	}
	
	

	public void setGraphData(int minute, int home_score, int away_score, int home_attach, int away_attach, int home_shots, int away_shots) {

//		System.out.println(this.graph_data[SCORE_HOME][minute]);
//		System.out.println(this.graph_data[SCORE_AWAY][minute]);

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

//		System.out.println(this.graph_data[SCORE_HOME][minute]);
//		System.out.println(this.graph_data[SCORE_AWAY][minute]);

	}

	public void printData() {
		System.out.println(this.graph_data[SCORE_HOME][92]);
		System.out.println(this.graph_data[SCORE_HOME][93]);
		System.out.println(this.graph_data[SCORE_HOME][94]);
		System.out.println(this.graph_data[SCORE_HOME][95]);
		System.out.println(this.graph_data[SCORE_HOME][96]);
		System.out.println(this.graph_data[SCORE_HOME][97]);

		System.out.println("HEHEHE");

		System.out.println(this.graph_data[SCORE_AWAY][92]);
		System.out.println(this.graph_data[SCORE_AWAY][93]);
		System.out.println(this.graph_data[SCORE_AWAY][94]);
		System.out.println(this.graph_data[SCORE_AWAY][95]);
		System.out.println(this.graph_data[SCORE_AWAY][96]);
		System.out.println(this.graph_data[SCORE_AWAY][97]);

	}

	public String toString() {
		return "Match:" + league + " --- " + startTime + " ---:" + match_status_minutes + ":--- " + match_home + " --- "
				+ score + " --- " + match_away + " --- " + match_handicap + " --- " + corner + " --- " + corner_line
				+ " --- " + total_goals + " --- " + match_attach + " --- " + match_shoot + " --- " + live_events
				+ " --- " + COL;
	}

}
