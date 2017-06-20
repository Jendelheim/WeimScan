
public class LogicController {

	
	public boolean halftimeBreak(String match_status_minutes){
		if(match_status_minutes == "Half"){
			return true;
		}
		else return false; 
	}
	
	public int whichQuarter(int min) {
		if (min > 0 && min < 16) {
			return 1;
		}
		else if (min > 15 && min < 31){
			return 2; 
		}
		else if (min > 30 && min < 45){
			return 3; 
		}
		else if (min > 45 && min < 61){
			return 4; 
		}
		else if (min > 60 && min < 76){
			return 5; 
		}
		else if (min > 75 && min < 91){
			return 6; 
		}
		else if (min > 90){
			return 7; // Skulle kunna sättas som en String "Overtime" beroende på var man vill använda den. 
		}
		else return -1; 
	}
	
	public double inconsistencyScore(int match_goal_home, int match_goal_away){
		double inconsistency = -1;  
		if(match_goal_home > match_goal_away){
			inconsistency = match_goal_home/match_goal_away; 
		}
		else if (match_goal_home < match_goal_away){
			inconsistency = match_goal_away/match_goal_home; 
		}
		return inconsistency;
	}
	
	public double inconsistencyShotsOnTarget(int match_attach_home, int match_attach_away){
		double inconsistency = -1;  
		if(match_attach_home > match_attach_away){
			inconsistency = match_attach_home/match_attach_away; 
		}
		else if (match_attach_home < match_attach_away){
			inconsistency = match_attach_away/match_attach_home; 
		}
		return inconsistency;
	}

}
