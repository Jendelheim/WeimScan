
public class Match {
	String league; // Param 1
	String startTime; // Param 2

	String match_status_minutes; // Param 3
	int minutes;

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
	}

	// Är det rimligt att göra en metod som denna för att inte behöva assigna
	// alla värden i onödan utan endast dem man testar?
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

			
		
			
			
	}

	public String toString() {
		return "Match:" + league + " --- " + startTime + " --- " + match_status_minutes + " --- " + match_home + " --- "
				+ score + " --- " + match_away + " --- " + match_handicap + " --- " + corner + " --- " + corner_line
				+ " --- " + total_goals + " --- " + match_attach + " --- " + match_shoot + " --- " + live_events
				+ " --- " + COL;
	}
}
