import org.omg.Messaging.SyncScopeHelper;
import java.util.InputMismatchException;

public class Match {

	// Tilldela alla v�rden (kanske endast de som kan vara null i senare skede
	// till -1
	private String league; // Param 1
    private String startTime; // Param 2

    private	String match_status_minutes; // Param 3
    private int minutes;
    private boolean started;

	// Hur parsear man s� man kontrollerar om det �r ett gult eller r�tt kort i
	// str�ngen?
    private String match_home; // Param 4
	// String hometeam;
    private int yellow_card_home;
    private int red_card_home;

    private String score; // Param 5
    private int home_score;
    private int away_score;

    private String match_away; // Param 6
	// String awayteam;
    private int yellow_card_away;
    private int red_card_away;

    private String match_handicap; // Param 7
    private double current_handicap;
    private double onbefore_handicap;

    private String corner; // Param 8
    private String corner_line; // Param 9

    private String total_goals; // Param 10
    private double current_goal_line;
    private double onbefore_goal_line;
    private double current_goal_line_half;
    private double onbefore_goal_line_half;

    private String match_attach; // Param 11
    private int home_attach;
    private int away_attach;

    private String match_shoot; // Param 12
    private int home_shoot;
    private int away_shoot;

    private	String live_events; // Param 13
    private String COL; // Param 14

    private String c_analysis;
    private String o_analysis;
    private String l_analysis;




    private  int[][] graph_data = new int[6][121];
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

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;

		if (!(other instanceof Match))
			return false;
		Match otherMatch = (Match) other;

		if (this.getHomeTeam().equals(otherMatch.getHomeTeam())) {
			return true;
		} else
			return false;
	}

	// �r det rimligt att g�ra en metod som denna f�r att inte beh�va assigna
	// alla v�rden i on�dan utan endast dem man testar?

	// M�ste kunna uppdatera samma objekt utan att skapa ett nytt, passa med
	// v�rden i assignProp s� man kan �ndra om.

	public void assignProperties(String match_status_minutes, String score, String match_handicap, String total_goals,
			String match_attach, String match_shoot) {

		System.out.println("OMG OMG OMG" + match_handicap);
		if (match_status_minutes.matches("\\d+")) {
			minutes = Integer.parseInt(match_status_minutes);
		}

		System.out.println("TESTETSTEST");
		String[] goals = score.split(" - ");
		System.out.println("TESTETETET" + goals[0]);
		home_score = Integer.parseInt(goals[0]);
		away_score = Integer.parseInt(goals[1]);

		// Logger.finest("home_score: " + home_score);
		// Logger.finest("away_score: " + away_score);
		//
		// Logger.finest("testing match_handicap: " + match_handicap);
		String[] handicaps = match_handicap.split(" ");

		if (!match_handicap.isEmpty()) {
			current_handicap = Double.parseDouble(handicaps[0].replaceAll("[^\\d.]", ""));

			if (handicaps.length > 1) {
				onbefore_handicap = Double.parseDouble(handicaps[1].replaceAll("[^\\d.]", ""));
			}

		}

		String[] goal_lines = total_goals.split(" ");

		if (!total_goals.isEmpty()) {

		    // Om matchen endast har ett pre-value på goalline tex. så fångas den i NFE då den
            // inte är en double utan "(4.5", funkar detta att göra så? what could go wrong?
		    try{
                current_goal_line = Double.parseDouble(goal_lines[0]);
            }
			catch(NumberFormatException ime) {
                ime.printStackTrace();
		    }

			if (goal_lines.length > 1) {
				onbefore_goal_line = Double.parseDouble(goal_lines[1].replaceAll("[^\\d.]", ""));
			}

			if (goal_lines.length == 4) {
				current_goal_line_half = Double.parseDouble(goal_lines[2]);
				onbefore_goal_line_half = Double.parseDouble(goal_lines[3].replaceAll("[^\\d.]", ""));
			}
		}

		try {
			String[] attachs = match_attach.split(" - ");
			home_attach = Integer.parseInt(attachs[0]);
			away_attach = Integer.parseInt(attachs[1]);
		} catch (final NumberFormatException ex) {
			home_attach = 0;
			away_attach = 0;
		}

		try {
			String[] shoots = match_shoot.split(" - ");
			home_shoot = Integer.parseInt(shoots[0]);
			away_shoot = Integer.parseInt(shoots[1]);
		} catch (final NumberFormatException ex) {
			home_attach = 0;
			away_attach = 0;
		}

	}

	public String getLeague() { return league; }

	public String getStartTime() { return startTime; }

	public int getMinutes() {
		return minutes;
	}

	public int getHomeScore() {
		return home_score;
	}

	public int getAwayScore() {
		return away_score;
	}

	public int getHomeAttach() {
		return home_attach;
	}

	public int getAwayAttach() {
		return away_attach;
	}

	public int getHomeShots() {
		return home_shoot;
	}

	public int getAwayShots() {
		return away_shoot;
	}

	public int getYellow_card_home() { return yellow_card_home; }

	public int getRed_card_home() { return red_card_home; }

	public int getYellow_card_away() { return yellow_card_away; }

	public int getRed_card_away() { return red_card_away; }

	public double getCurrent_handicap() { return current_handicap; }

	public double getOnbefore_handicap() { return onbefore_handicap; }

    public double getCurrent_goal_line() { return current_goal_line;  }

    public double getOnbefore_goal_line() { return onbefore_goal_line; }

    public double getCurrent_goal_line_half() { return current_goal_line_half; }

    public double getOnbefore_goal_line_half() { return onbefore_goal_line_half; }



	public String getHomeTeam() {
		return match_home;
	}

	public String getAwayTeam() {
		return match_away;
	}

	public String getScore() {
		return score;
	}

	public String getMinuteString() {
		return match_status_minutes;
	}

	public String getMatchHandicap() {
		return match_handicap;
	}

	public String getTotalGoals() {
		return total_goals;
	}

	public String getMatchAttach() {
		return match_attach;
	}

	public String getMatchShoot() {
		return match_shoot;
	}






	public void setGraphData(int minute, int home_score, int away_score, int home_attach, int away_attach,
			int home_shots, int away_shots) {

		// Logger.finest(this.graph_data[SCORE_HOME][minute]);
		// Logger.finest(this.graph_data[SCORE_AWAY][minute]);

		// Logger.finest("A1: " + minute);
		// Logger.finest("A2: " + home_score);
		// Logger.finest("A3: " + away_score);
		// Logger.finest("A4: " + home_attach);
		// Logger.finest("A5: " + away_attach);
		// Logger.finest("A6: " + home_shots);
		// Logger.finest("A7: " + away_shots);

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

		// Logger.finest("TESTETETETET");
		// Logger.finest(this.graph_data[SCORE_HOME][minute]);
		// Logger.finest(this.graph_data[SCORE_AWAY][minute]);

	}

	public void printGraphData() {
		Logger.info("printGraphData-start at minute: " + minutes + " " + this.toString());
		for (int i = 0; i < 6; i++) {
			Logger.finest(i + " ");
			for (int j = 0; j < 121; j++) {
				System.out.print(graph_data[i][j]); // println skriver ut alla
														// index f�r sig s� man
														// kan se r�tt
														// tilldelning.
			}
		}

		Logger.info("\nprintGraphData-end");
	}

	public void printData(int minute) {

		// Logger.finest(this.graph_data[SCORE_HOME][minute -2]);
		// Logger.finest(this.graph_data[SCORE_HOME][minute -1]);
		// Logger.finest(this.graph_data[SCORE_HOME][minute]);
		// Logger.finest(this.graph_data[SCORE_HOME][minute +1]);
		// Logger.finest(this.graph_data[SCORE_HOME][minute +2]);
		//
		// Logger.finest("HEHEHE");
		//
		//// Logger.finest(this.graph_data[SCORE_AWAY][minute -2]);
		//// Logger.finest(this.graph_data[SCORE_AWAY][minute -1]);
		// Logger.finest(this.graph_data[SCORE_AWAY][minute]);
		// Logger.finest(this.graph_data[SCORE_AWAY][minute +1]);
		// Logger.finest(this.graph_data[SCORE_AWAY][minute +2]);

	}

	public String toString() {
		return "Match: " + league + " --- " + startTime + " ---:" + match_status_minutes + ":--- " + match_home
				+ " --- " + score + " --- " + match_away + " --- " + match_handicap + " --- " + corner + " --- "
				+ corner_line + " --- " + total_goals + " --- " + match_attach + " --- " + match_shoot + " --- "
				+ live_events + " --- " + COL;
	}

}
