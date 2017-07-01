import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//class Runner extends Thread {
//
//	@Override
//	public void run() {
//
//	}
//
//}

public class MainProgram {
	static MainProgram program = new MainProgram();
	private LogicController logic = new LogicController();
	static ArrayList<Match> matches = new ArrayList<>();

	// number of threads
	public static final int THREADS = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws IOException {
		Logger.info("Threads: " + THREADS);

		// Timer timer = new Timer();
		// timer.schedule(new Receiver(), 0, 6000);

		program.addThread().start();
		program.initializeScanner();

		// System.err.println("totalCorner();");
		// program.totalCorner();
		//
		// System.err.println("liveMatch();");
		// program.liveMatch();
		// System.err.println("print_live_matches();");
		// program.print_live_matches();
		//
		// System.err.println("returnFirstLiveMatch();");
		// program.returnFirstLiveMatch();

		Logger.info("FINISHED!!");

	}

	public static void assignProperties(Match storedMatch, Match match) {
		// System.out.println(match);
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
			addThread().start();
			initializeScanner();
			break;

		// Print graphData for each live game
		case 2:
			Logger.finest("2: Print graphData");
			for (Match m : liveMatch()) {
				m.printGraphData();
			}
			initializeScanner();
			break;

//		case 3:
// ??
//			break;
		default:
			// TODO handle this case
			// TODO Got to close the keyboard too before exiting.
			keyboard.close();
			break;
		}
	}

	public Thread addThread() {

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 1; i++) {
					Document document = null;
					Logger.fine("STARTED THREAD EL NUMBERO DEL: " + i);
					try {
						document = Jsoup.connect("http://www.totalcorner.com/match/today").get();
					} catch (IOException e) {
						Logger.info("Connection Error in jsoup module");
						e.printStackTrace();
					}

					Logger.info("scrapeMatchlist();");
					scrapeMatchlist(document, "#inplay_match_table tbody:last-of-type td");

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						Logger.err("Thread sleep error");
						e.printStackTrace();
					}
				}

			}

			private ArrayList<String> scrapeMatchlist(Document document, String selector) {
				Elements elements = document.select(selector);
				ArrayList<String> values = new ArrayList<>();
				for (Element element : elements) {
					String str = element.text();
					values.add(str);

				}

				Logger.fine("createMatchObjects();");
				createMatchObjects(values);

				return values;
			}

			public void createMatchObjects(ArrayList<String> values) {

				final int NUM_PARAM = 14;
				if (values.size() % NUM_PARAM != 0) {
					Logger.err("Expected values to be divisible by 14. They are " + values.size() + " instead");
				}

				int totMatches = values.size() / NUM_PARAM;
				for (int i = 0; i < totMatches; i++) {
					String[] params = new String[NUM_PARAM];
					for (int j = 0; j < NUM_PARAM; j++) {
						params[j] = values.get(i * NUM_PARAM + j);
					}
					Logger.finest("Heloaaaa");
					Match match = new Match(params);
					Logger.finest("testing - match: " + match.toString());
					String str = Arrays.toString(params);
					Logger.finest("testing - params: " + str);

					if (!matchDuplicate(match)) {
						matches.add(match);

					}

					Logger.fine("assignProperties();");
					assignProperties(match, match);

					Logger.fine("setGraphData();" + match);
					setGraphData(match, match);

				}

				Logger.err("createMatchObjects(); finished!");
			}

			public boolean matchDuplicate(Match match) {
				boolean duplicate = false;

				Logger.info("matchDuplicate(); printing matchtes: " + matches);
				for (Match matchCheck : matches) {
					if (match.equals(matchCheck)) {
						Logger.finest("TRUE");
						duplicate = true;

						Logger.finest("assignProperties();" + "INNE I LOOPFAN");
						assignProperties(matchCheck, match);

						Logger.info("setGraphData();");
						setGraphData(matchCheck, match);
					}
					// else System.err.println("FALSE" + " " +
					// match.getHomeTeam() + " " + matchCheck.getHomeTeam() + "
					// /// " + match.getAwayTeam() + " " +
					// matchCheck.getAwayTeam() );
				}
				Logger.info("matchDuplicate(); finished!" + duplicate);
				return duplicate;
			}

		});
		return t1;

	}

	public static void setGraphData(Match storedMatch, Match match) {

		storedMatch.setGraphData(match.getMinutes(), match.getHomeScore(), match.getAwayScore(), match.getHomeAttach(),
				match.getAwayAttach(), match.getHomeShots(), match.getAwayShots());

		Logger.err("setGraphData(); finished!");
	}

	public ArrayList<Match> liveMatch() {

		ArrayList<Match> live_matches = new ArrayList<>();

		Logger.info("liveMatch(); started");
		for (Match m : matches) {
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

	// Kan �ndras till return_live_matches() sen d� man ska h�mta dom till
	// hemsidan
	public void print_live_matches() {
		ArrayList<Match> liveMatches = liveMatch();

		for (Match m : liveMatches) {

			Logger.info(m.toString());
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

	public void loop(ArrayList<Match> matches) {
		for (Match match : matches) {
			System.out.println(match);
			Logger.finest("match done");
		}
		Logger.info("matches loop done");
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

	public <T> void printMessage(String message, T value) {
		Logger.info(message + value);
	}

}

// static class Receiver extends TimerTask {
//
// ArrayList<String> receivedArrayList = new ArrayList<String>();
//
// @Override
// public void run() {
// System.out.println("Hello world - begins");
// try {
// totalCorner();
// } catch (IOException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//
// System.out.println(receivedArrayList);
//
// System.out.println("hello world - ends");
//
// }
//
// public void totalCorner() throws IOException {
// Document document =
// Jsoup.connect("http://www.totalcorner.com/match/today").get();
// receivedArrayList = scrapeMatchlist(document, "#inplay_match_table
// tbody:last-of-type td");
// // loop(matches);
// // liveMatch();
// }
//
// private ArrayList<String> scrapeMatchlist(Document document, String
// selector) {
// Elements elements = document.select(selector);
// ArrayList<String> values = new ArrayList<>();
//
// for (Element element : elements) {
// // System.out.println("1");
// String str = element.text();
// // System.out.println(str);
// values.add(str);
// }
//
// // createMatchObjects(values);
// return values;
// }
//
// }

// public void totalCorner() throws IOException {
// Document document =
// Jsoup.connect("http://www.totalcorner.com/match/today").get();
//
// System.err.println("scrapeMatchlist();");
// scrapeMatchlist(document, "#inplay_match_table tbody:last-of-type td");
// // loop(matches);
//
// }
//
// private ArrayList<String> scrapeMatchlist(Document document, String selector)
// {
// Elements elements = document.select(selector);
// ArrayList<String> values = new ArrayList<>();
// for (Element element : elements) {
// String str = element.text();
// values.add(str);
//
// }
//
// System.err.println("createMatchObjects();");
// createMatchObjects(values);
// return values;
// }
//
// public void createMatchObjects(ArrayList<String> values) {
// matches.clear();
// final int NUM_PARAM = 14;
// if (values.size() % NUM_PARAM != 0) {
// System.out.println("Expected values to be divisible by 14. They are " +
// values.size() + " instead");
// }
//
// int totMatches = values.size() / NUM_PARAM;
// for (int i = 0; i < totMatches; i++) {
// String[] params = new String[NUM_PARAM];
// for (int j = 0; j < NUM_PARAM; j++) {
// params[j] = values.get(i * NUM_PARAM + j);
// }
// System.out.println("Heloaaaa");
// Match match = new Match(params);
// System.out.println("testing - match: " + match.toString());
// String str = Arrays.toString(params);
// System.out.println("testing - params: " + str);
//
// if (!matchDuplicate(match)) {
// // System.err.println("matchDuplicate(); check: " +
// // matchDuplicate(match) + " Match: " + match);
// matches.add(match);
//
// System.err.println("assignProperties();");
// assignProperties(match);
//
// System.err.println("setGraphData();" + match);
// setGraphData(match);
// }
//
// }
//
// System.err.println("createMatchObjects(); finished!");
// }
//
// public boolean matchDuplicate(Match match) {
// boolean duplicate = false;
// // System.out.println("Printing match.getHomeTeam(): " +
// // match.getHomeTeam());
// // System.out.println("Printing match.getAwayTeam(): " +
// // match.getAwayTeam());
//
// System.out.println("matchDuplicate(); printing matchtes: " + matches);
// for (Match matchCheck : matches) {
// if (match.getHomeTeam() == matchCheck.getHomeTeam() && match.getAwayTeam() ==
// matchCheck.getAwayTeam()) {
//
// // System.out.println("var1 " + match.getHomeTeam());
// // System.out.println("var2 " + matchCheck.getHomeTeam());
// // System.out.println("var3 " + match.getAwayTeam());
// // System.out.println("var3 " + matchCheck.getHomeTeam());
//
// duplicate = true;
// // System.out.println("duplicate: " + duplicate);
// // System.out.println("matchDuplicate() input: " + match);
// // System.out.println("matchCheck: " + matchCheck);
//
// System.err.println("assignProperties();");
// assignProperties(match);
//
// System.err.println("setGraphData();");
// setGraphData(match);
// }
// }
// System.err.println("matchDuplicate(); finished!");
// return duplicate;
// }
