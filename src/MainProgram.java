import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.jsoup.nodes.Document;


public class MainProgram {
	static MainProgram program = new MainProgram();
	private LogicController logic = new LogicController();
	//private Thread currentThread;

	Parser parser = new Parser();

	/* Used to prevent excessive requests */
	long documentTimestamp = System.currentTimeMillis();
	Document savedDocument = null;
	private static final long REQUEST_GRACE_TIME = 1000 * 60 * 3; // 10 min

	// number of threads
	public static final int THREADS = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws IOException {
		Logger.info("Threads: " + THREADS);


		program.parser.addThread().start();
		program.initializeScanner();

		Logger.info("FINISHED!!");

	}

	public static void assignProperties(Match storedMatch, Match match) {
		storedMatch.assignProperties(match.getMinuteString(), match.getScore(), match.getMatchHandicap(),
				match.getTotalGoals(), match.getMatchAttach(), match.getMatchShoot());

		Logger.fine("assignProperties(); finished!");
	}

	public void initializeScanner() {
		Scanner keyboard = new Scanner(System.in);
		Logger.info("enter an integer");

		int input = keyboard.nextInt();

		switch (input) {

		// Start parsing
		case 1:
			Logger.finest("1: parsing");
			parser.addThread().start();
			initializeScanner();
			System.out.println("hejejejej");
			break;

		// Print graphData for each live game
		case 2:
			Logger.finest("2: Print graphData");
			for (Match m : liveMatch()) {
				m.printGraphData();
			}
			initializeScanner();
			break;

		 case 3:
		     parser.getAllMatches();
		     initializeScanner();
		 break;

		 case 4:
		 	System.out.print("Enter match ID: ");

			 int ID = keyboard.nextInt();
			 parser.getSpecificMatchAwayAttach(ID);
			 initializeScanner();
			 break;

		default:
			// TODO handle this case
			// TODO Got to close the keyboard too before exiting.
			keyboard.close();
			break;
		}
	}

	public static void setGraphData(Match storedMatch, Match match) {
		storedMatch.setGraphData(match.getMinutes(), match.getHomeScore(), match.getAwayScore(), match.getHomeAttach(),
				match.getAwayAttach(), match.getHomeShots(), match.getAwayShots());

		Logger.err("setGraphData(); finished!");
	}

	public ArrayList<Match> liveMatch() {
		ArrayList<Match> live_matches = new ArrayList<>();

		Logger.info("liveMatch(); started");
		for (Match m : parser.matches) {
			if (m.getMinutes() > -1) {
				live_matches.add(m);
			} else {
				Logger.err("Match has negative minutes!");
			}
		}

		Logger.info("liveMatch(); finished!");
		return live_matches;
	}

	public Match returnFirstLiveMatch() {
		Match match;
		match = liveMatch().get(0);
		printMessage("returnFirstLiveMatch(): " + match, null);

		Logger.err("Testing graphPrint");
		match.printGraphData();
		return match;
	}

	public void print_live_matches() {
		ArrayList<Match> liveMatches = liveMatch();

		for (Match m : liveMatches) {

			Logger.info("livematch: " + m.toString());
			assignProperties(m, m);
			setGraphData(m, m);

			Logger.finest("home_score" + m.getHomeScore());
			Logger.finest("home_attach" + m.getHomeAttach());
			Logger.finest("home_shots" + m.getHomeShots());

			Logger.finest("away_score" + m.getAwayScore());
			Logger.finest("away_attach" + m.getAwayAttach());
			Logger.finest("away_shots" + m.getAwayShots());

			Logger.err("calculateInconsistencies();");
			calculateInconsistencies(m);

			Logger.info("print_live_matches(); finished!");
		}
	}

	public void calculateInconsistencies(Match match) {
		printMessage("testing calc.match: " + match, null);
		printMessage("hello my name is ", match.getAwayTeam());
		Logger.info(match.getHomeTeam() + " vs " + match.getAwayTeam());
		logic.inconsistency(match.getHomeScore(), match.getAwayScore());
		logic.inconsistency(match.getHomeAttach(), match.getAwayAttach());
		logic.inconsistency(match.getHomeShots(), match.getAwayShots());
		Logger.info("testing graphData: ");
		match.printData(match.getMinutes());

		Logger.info("calculateInconsistencies(); finished!");

	}

	 // Måste göra om så get-metoderna från parser returnerar en int/double istället för void.
	public double[] calculateScoreInconsistency(int ID){
			logic.inconsistency(parser.getSpecificMatchHomeScore(ID), parser.getSpecificMatchAwayScore(ID));
	}

	public double[] calculateAttachInconsistency(int ID){
			logic.inconsistency(parser.getSpecificMatchHomeAttach(ID), parser.getSpecificMatchAwayAttach(ID));
	}

	public double[] calculateShotsInconsistency(int ID){
			logic.inconsistency(parser.getSpecificMatchHomeShots(ID), parser.getSpecificMatchAwayShots(ID));
	}

	public <T> void printMessage(String message, T value) {
		Logger.info(message + value);
	}

}
