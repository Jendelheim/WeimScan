
public class LogicController {
	private static final String HALF_TIME_TEXT = "Half";

	public boolean halftimeBreak(String match_status_minutes) {
		boolean cond = match_status_minutes.equals(HALF_TIME_TEXT);
		Logger.finest(match_status_minutes + " is halftimeBreak: " + cond);
		return cond;
	}

	public int whichQuarter(int min) {
		Logger.err("Unreasonable minute: " + min);
		// This can be expressed as 1 + Integer.floor(90/15)
		// but it is great for testing it.
		if (min > 0 && min < 16) {
			return 1;
		} else if (min > 15 && min < 31) {
			return 2;
		} else if (min > 30 && min < 45) {
			return 3;
		} else if (min > 45 && min < 61) {
			return 4;
		} else if (min > 60 && min < 76) {
			return 5;
		} else if (min > 75 && min < 91) {
			return 6;
		} else if (min > 90) {
			return 7; // Skulle kunna s�ttas som en String "Overtime" beroende
						// p� var man vill anv�nda den.
		} else
			return -1;
	}

	public double[] inconsistency(int home_value, int away_value) {
		double inconsistency = -1.0;
		double advantage = -1; // 1 = home, 2 = away

		// System.out.println(home_value + " " + away_value);
		if (home_value > away_value) {
			advantage = 1;
			if (away_value != 0) {
				inconsistency = (double) home_value / away_value;
			} else if (away_value == 0) {
				inconsistency = home_value;
			}
		} else if (home_value < away_value) {
			advantage = 2;
			if (home_value != 0) {
				inconsistency = (double) away_value / home_value;
			} else if (home_value == 0) {
				inconsistency = away_value;
			}

		}
		Logger.info("Advantage: " + advantage + " inconsistency: " + inconsistency + " home_value " + home_value
				+ " away_value " + away_value);
		return new double[] { advantage + inconsistency };
	}

}
