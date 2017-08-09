		
public class LogicController {

	Parser parser = new Parser();
	private static final String HALF_TIME_TEXT = "Half";

	public boolean halftimeBreak(String match_status_minutes) {
		boolean cond = match_status_minutes.equals(HALF_TIME_TEXT);
		Logger.finest(match_status_minutes + " is halftimeBreak: " + cond);
		return cond;
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

	// get all inconsistencies for specific ID

//	public double getScoreInconsistency(){
//		double scoreIncon;
//		double[] values= inconsistency(1,1 );
//		scoreIncon = values[0];
//		return scoreIncon;
//	}
//
//	public double getAttachInconsitency(){
//		double attachIncon;
//		double[] values= inconsistency(1,1 );
//		attachIncon = values[0];
//		return attachIncon;
//
//	}
//
//	public double getShotInconsitency(){
//		double shotIncon;
//		double[] values= inconsistency(1,1 );
//		shotIncon = values[0];
//		return shotIncon;
//	}

	// Måste göra om så get-metoderna från parser returnerar en int/double istället för void.

//	public double[] calculateScoreInconsistency(int ID){
//		double[] values = inconsistency(parser.getSpecificMatchHomeScore(ID), parser.getSpecificMatchAwayScore(ID));
//		return values;
//	}
//
//	public double[] calculateAttachInconsistency(int ID){
//		double[] values = inconsistency(parser.getSpecificMatchHomeAttach(ID), parser.getSpecificMatchAwayAttach(ID));
//		return values;
//	}
//
//	public double[] calculateShotsInconsistency(int ID){
//		double[] values = inconsistency(parser.getSpecificMatchHomeShots(ID), parser.getSpecificMatchAwayShots(ID));
//		return values;
//	}
//

}
