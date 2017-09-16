import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


import java.sql.*;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Created by Victor on 2017-07-13.
 */
public class Parser {

    // SQL

    // live_matches
    private static final String GET_ALL_MATCHES = "SELECT * FROM live_matches";
    private static final String INSERT_NEW_LIVE_MATCH = "INSERT INTO live_matches (ID, league, start_time, minutes, home_team, yellow_card_home, red_card_home, home_score, away_team, yellow_card_away, red_card_away, away_score, current_handicap, onbefore_handicap, current_goal_line, onbefore_goal_line, current_goal_line_half, onbefore_goal_line_half, home_attach, away_attach, home_shoot, away_shoot)VALUES (?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,? ,?,?)";
    private static final String GET_ALL_LIVE_MATCHES = "SELECT * FROM live_matches WHERE minutes > 0";
    private static final String ALREADY_IN_DATABASE = "SELECT * FROM live_matches WHERE home_team = ?;";
    private static final String UPDATE_LIVE_MATCH = "UPDATE live_matches SET league = ?, start_time = ?, minutes = ?, home_team = ?, yellow_card_home = ?, red_card_home = ?, home_score = ?, away_team = ?, yellow_card_away = ?, red_card_away = ?, away_score = ?, current_handicap = ?, onbefore_handicap = ?, current_goal_line = ?, onbefore_goal_line = ?, current_goal_line_half = ?, onbefore_goal_line_half = ?, home_attach = ?, away_attach = ?, home_shoot = ?, away_shoot = ? WHERE ID = ?";

    private static final String GET_SPECIFIC_MATCH = "SELECT * WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_LEAGUE = "SELECT league FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_STARTTIME = "SELECT start_time FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_MINUTES = "SELECT minutes FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_HOMETEAM= "SELECT home_team FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_YELLOW_CARD_HOME = "SELECT yellow_card_home FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_RED_CARD_HOME = "SELECT red_card_home FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_HOMESCORE = "SELECT home_score FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_AWAYTEAM= "SELECT away_team FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_YELLOW_CARD_AWAY = "SELECT yellow_card_away FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_RED_CARD_AWAY = "SELECT red_card_away FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_AWAYSCORE = "SELECT away_score FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_CURRENT_HANDICAP = "SELECT current_handicap FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_ONBEFORE_HANDICAP = "SELECT onbefore_handicap FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_CURRENT_GOALLINE = "SELECT current_goal_line FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_ONBEFORE_GOALLINE = "SELECT onbefore_goal_line FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_CURRENT_GOALLINE_HALF = "SELECT current_goal_line_half FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_ONBEFORE_GOALLINE_HALF = "SELECT onbefore_goal_line_half FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_HOME_ATTACH = "SELECT home_attach FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_AWAY_ATTACH = "SELECT away_attach FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_HOME_SHOTS = "SELECT home_shoot FROM live_matches WHERE ID = ?";
    private static final String GET_SPECIFIC_MATCH_AWAY_SHOTS = "SELECT away_shoot FROM live_matches WHERE ID = ?";

    // graph_data
    private static final String INSERT_NEW_GRAPH_DATA = "INSERT INTO graph_data (match_id, match_minute, home_score, away_score, dangerous_attacks_home, dangerous_attacks_away, shots_home, shots_away) VALUES (?,?,?,?,? ,?,?)";
    private static final String UPDATE_GRAPH_DATA = "UPDATE graph_data SET league = ?, start_time = ?, minutes = ?, home_team = ?, yellow_card_home = ?, red_card_home = ?, home_score = ?, away_team = ?, yellow_card_away = ?, red_card_away = ?, away_score = ?, current_handicap = ?, onbefore_handicap = ?, current_goal_line = ?, onbefore_goal_line = ?, current_goal_line_half = ?, onbefore_goal_line_half = ?, home_attach = ?, away_attach = ?, home_shoot = ?, away_shoot = ? WHERE ID = ?";
    private static final String GET_GRAPH_DATA_FOR_MATCH = "SELECT * FROM graph_data WHERE match_id = ?;";
    private static final String GET_HOME_SCORE_GRAPH_DATA = "SELECT match_minute, home_score FROM graph_data WHERE match_id = ?";
    private static final String GET_AWAY_SCORE_GRAPH_DATA = "SELECT match_minute, away_score FROM graph_data WHERE match_id = ?";
    private static final String GET_DANGEROUS_ATTACKS_HOME = "SELECT match_minute, dangerous_attack_home FROM graph_data WHERE match_id = ?";
    private static final String GET_DANGEROUS_ATTACKS_AWAY = "SELECT match_minute, dangerous_attacks_away FROM graph_data WHERE match_id = ?";
    private static final String GET_SHOTS_HOME = "SELECT match_minute, shots_home FROM graph_data WHERE match_id = ?";
    private static final String GET_SHOTS_AWAY = "SELECT match_minute, shots_away FROM graph_data WHERE match_id = ?";

    private int startID = 0;

    static ArrayList<Match> matches = new ArrayList<>();

    /* Used to prevent excessive requests */
    long documentTimestamp = System.currentTimeMillis();
    private Document savedDocument = null;
    private static final long REQUEST_GRACE_TIME = 1000 * 60 * 3; // 10 min

    // number of threads
//    public static final int THREADS = Runtime.getRuntime().availableProcessors();

    public static DataSource getMySQLDataSource() { // Funkar denna som connect
        MysqlDataSource mysqlDS = null;

        try {
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL("jdbc:mysql://localhost:3306/weimscan");
            mysqlDS.setUser("root");
            mysqlDS.setPassword("tgcmxzqw9");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;

        // Get the MySqlDataSource

        System.out.println("Verifying access");
        DataSource dataSource = getMySQLDataSource();

        // Get connection from the database

        System.out.println("Connecting database...");
        connection = dataSource.getConnection();

        if(connection != null){
            System.out.println("Connection is not null!");
        }

        return connection;

    }

    public void closePrstmt(PreparedStatement prstmt) {
        try {
            if (prstmt != null) {
                prstmt.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public Thread addThread() {

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {

                Connection connection = null;

                try {
                    connection = getConnection();
                    System.out.println("database connected!");

                } catch(SQLException se){
                    se.printStackTrace();
                }

                for (int i = 0; i < 1; i++) {
                    Document document = null;
                    try {
                        document = getDocument();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Logger.fine("STARTED THREAD EL NUMBERO DEL: " + i);

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
        });

        return t1;
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

    public void checkExisting(Match match){
        PreparedStatement prstmt = null;
        Connection connection = null;
        try {
            connection = getConnection();

            prstmt = connection.prepareStatement(ALREADY_IN_DATABASE);

            prstmt.setString(1, match.getHomeTeam());

            ResultSet rs = prstmt.executeQuery();



            if(!rs.next()){
                System.out.println("No data found! - Inserting new match to database");
                insertLiveMatch(match);
            }
            else {
                int id = rs.getInt("ID");
                System.out.println("RESULTSET ID : " + id);

                System.out.println("Data found! - Updating data in database");
                updateLiveMatch(match, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closePrstmt(prstmt);
            closeConnection(connection);
        }

    }

    public void insertLiveMatch(Match match) {
        PreparedStatement prstmt = null;
        Connection connection = null;

        try {
            connection = getConnection();

            prstmt = connection.prepareStatement(INSERT_NEW_LIVE_MATCH);

            prstmt.setInt(1, startID++);
            prstmt.setString(2, match.getLeague());
            prstmt.setString(3, match.getStartTime());
            prstmt.setInt(4, match.getMinutes());
            prstmt.setString(5, match.getHomeTeam());
            prstmt.setInt(6, match.getYellow_card_home());
            prstmt.setInt(7, match.getRed_card_home());
            prstmt.setInt(8,match.getHomeScore());
            prstmt.setString(9, match.getAwayTeam());
            prstmt.setInt(10, match.getYellow_card_away());
            prstmt.setInt(11, match.getRed_card_away());
            prstmt.setInt(12, match.getAwayScore());
            prstmt.setDouble(13, match.getCurrent_handicap());
            prstmt.setDouble(14, match.getOnbefore_handicap());
            prstmt.setDouble(15, match.getCurrent_goal_line());
            prstmt.setDouble(16, match.getOnbefore_goal_line());
            prstmt.setDouble(17, match.getCurrent_goal_line_half());
            prstmt.setDouble(18, match.getOnbefore_goal_line_half());
            prstmt.setDouble(19, match.getHomeAttach());
            prstmt.setDouble(20, match.getAwayAttach());
            prstmt.setDouble(21, match.getHomeShots());
            prstmt.setDouble(22, match.getAwayShots());

            boolean rs = prstmt.execute(); // http://stackoverflow.com/questions/1905607/cannot-issue-data-manipulation-statements-with-executequery
            System.out.println(rs);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closePrstmt(prstmt);
            closeConnection(connection);
        }
    }

    public void updateLiveMatch(Match match, int id){
        PreparedStatement prstmt = null;
        Connection connection = null;
        try {
            connection = getConnection();

            prstmt = connection.prepareStatement(UPDATE_LIVE_MATCH);

            prstmt.setString(1, match.getLeague());
            prstmt.setString(2, match.getStartTime());
            prstmt.setInt(3, match.getMinutes());
            prstmt.setString(4, match.getHomeTeam());
            prstmt.setInt(5, match.getYellow_card_home());
            prstmt.setInt(6, match.getRed_card_home());
            prstmt.setInt(7,match.getHomeScore());
            prstmt.setString(8, match.getAwayTeam());
            prstmt.setInt(9, match.getYellow_card_away());
            prstmt.setInt(10, match.getRed_card_away());
            prstmt.setInt(11, match.getAwayScore());
            prstmt.setDouble(12, match.getCurrent_handicap());
            prstmt.setDouble(13, match.getOnbefore_handicap());
            prstmt.setDouble(14, match.getCurrent_goal_line());
            prstmt.setDouble(15, match.getOnbefore_goal_line());
            prstmt.setDouble(16, match.getCurrent_goal_line_half());
            prstmt.setDouble(17, match.getOnbefore_goal_line_half());
            prstmt.setDouble(18, match.getHomeAttach());
            prstmt.setDouble(19, match.getAwayAttach());
            prstmt.setDouble(20, match.getHomeShots());
            prstmt.setDouble(21, match.getAwayShots());
            prstmt.setInt(22, id);


            prstmt.executeUpdate(); // http://stackoverflow.com/questions/1905607/cannot-issue-data-manipulation-statements-with-executequery

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closePrstmt(prstmt);
            closeConnection(connection);
        }
    }

    public void getAllMatches(){
        PreparedStatement prstmt = null;
        Connection connection = null;

        try {
            connection = getConnection();

            prstmt = connection.prepareStatement(GET_ALL_MATCHES);


            ResultSet rs= prstmt.executeQuery(); // http://stackoverflow.com/questions/1905607/cannot-issue-data-manipulation-statements-with-executequery
            printingResultSet(rs, " getAllMatches() ");
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closePrstmt(prstmt);
            closeConnection(connection);
        }
    }

    public void  getGetAllLiveMatches() {
    }


    // get all stats for specific ID

//
//    public void getSpecificMatch(int ID){
//       genericDatabaseCall(ID, GET_SPECIFIC_MATCH, " getSpecificMatch ");
//    }

    public String getSpecificMatchLeague(int ID){
       return stringDatabaseCall(ID, GET_SPECIFIC_MATCH_LEAGUE, " getSpecificMatchLeague ");
    }

    public String getSpecificMatchStartTime(int ID){
        return stringDatabaseCall(ID, GET_SPECIFIC_MATCH_STARTTIME, " getSpecificMatchStartTime ");
    }

    public int getSpecificMatchMinutes(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_MINUTES, " getSpecificMatchMinutes ");
    }

    public String getSpecificMatchHomeTeam(int ID){
        return stringDatabaseCall(ID, GET_SPECIFIC_MATCH_HOMETEAM, " getSpecificMatchHomeTeam ");
    }

    public int getYellowCardHome(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_YELLOW_CARD_HOME, " getSpecificMatchYellowCardHome ");
    }

    public int getRedCardHome(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_RED_CARD_HOME, " getSpecificMatchRedCardHome ");
    }

    public int getHomeScore(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_HOMESCORE, " getSpecificMatchHomeScore ");
    }

    public String getAwayTeam(int ID){
        return stringDatabaseCall(ID, GET_SPECIFIC_MATCH_AWAYTEAM, " getSpecificMatchAwayTeam ");
    }

    public int getYellowCardAway(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_YELLOW_CARD_AWAY, " getSpecificMatchYellowCardAway ");
    }

    public int getRedCardAway(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_RED_CARD_AWAY, " getSpecificMatchRedCardAway ");
    }

    public int getAwayScore(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_AWAYSCORE, " getSpecificMatchAwayScore ");
    }

    public double getCurrentHandicap(int ID){
        return doubleDatabaseCall(ID, GET_SPECIFIC_MATCH_CURRENT_HANDICAP, " getSpecificMatchCurrentHandicap ");
    }

    public double getOnbeforeHandicap(int ID){
        return doubleDatabaseCall(ID, GET_SPECIFIC_MATCH_ONBEFORE_HANDICAP, " getSpecificMatchOnbeforeHandicap ");
    }

    public double getCurrentGoalLine(int ID){
        return doubleDatabaseCall(ID, GET_SPECIFIC_MATCH_CURRENT_GOALLINE, " getSpecificMatchCurrentGoalLine ");
    }

    public double getOnbeforeGoalLine(int ID){
        return doubleDatabaseCall(ID, GET_SPECIFIC_MATCH_ONBEFORE_GOALLINE, " getSpecificMatchOnbeforeGoalLine ");
    }

    public double getCurrentGoalLineHalf(int ID){
        return doubleDatabaseCall(ID, GET_SPECIFIC_MATCH_CURRENT_GOALLINE_HALF, " getSpecificMatchCurrentGoalLineHalf ");
    }

    public double getOnbeforeGoalLineHalf(int ID){
        return doubleDatabaseCall(ID, GET_SPECIFIC_MATCH_ONBEFORE_GOALLINE_HALF, " getSpecificMatchOnbeforeGoalLineHalf ");
    }

    public int getHomeAttach(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_HOME_ATTACH, " getSpecificMatchAwayAttach ");
    }

    public int getAwayAttach(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_AWAY_ATTACH, " getSpecificMatchAwayAttach ");
    }

    public int getHomeShots(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_HOME_SHOTS, " getSpecificMatchHomeShots ");
    }

    public int getAwayShots(int ID){
        return intDatabaseCall(ID, GET_SPECIFIC_MATCH_AWAY_SHOTS, " getSpecificMatchAwayShots ");
    }

    // get graph_data

    // Vad ska dom egentligen returna? Ska det vara enkla ints eller ska det vara arrayer med ints?
    // Eller ska det kanske vara en int men inte bara med villkoret ID utan även minut?

//    public int[] getAllData(int ID){
//        int[] data = null;
//
//        return data;
//    }
//
//    public int getMatchMinute(int ID){
//        return intDatabaseCall(ID, );
//    }
//
//    public int getScoreHome(int ID){
//        return;
//    }
//
//    public int getScoreAway(int ID){
//        return;
//    }
//
//    public int getAttachHome(int ID){
//        return;
//    }
//
//    public int getAttachAway(int ID){
//        return;
//    }
//
//    public int getShotsHome(int ID){
//        return;
//    }
//
//    public int getShotsAway(int ID){
//        return;
//    }






//
//
//    // get all inconsistencies for specific ID
//
//    public void getScoreInconsistency(){
//
//    }
//
//    public void getAttachInconsitency(){
//
//    }
//
//    public void getShotInconsitency(){
//
//    }
//


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


            // Checker if the game is already in the database.





            Logger.fine("assignProperties();");
            assignProperties(match, match);

            Logger.fine("setGraphData();" + match);
            setGraphData(match, match);
            checkExisting(match);

        }

        Logger.err("createMatchObjects(); finished!");
    }

    protected Document getDocument() throws IOException {
//        File input = new File("C:\\Users\\Victo\\Downloads\\matchList.html");
//
//            Document document = Jsoup.parse(input, "UTF-8", "http://totalcorner.com/today");

        Document document = null;
        long timeNow = System.currentTimeMillis();

        // request for the first time and after grace delay
        if (savedDocument != null && (REQUEST_GRACE_TIME > (timeNow - documentTimestamp))) {
            Logger.finest("Graced document returned");
            return savedDocument;
        } else {
            try {
                Logger.finest("Requesting document");
                document = Jsoup.connect("http://www.totalcorner.com/match/today").get();
                savedDocument = document;
                documentTimestamp = System.currentTimeMillis();
            } catch (IOException e) {
                Logger.info("Connection Error in jsoup module");
                e.printStackTrace();
            }
        }
        return document;
    }

    public static void setGraphData(Match storedMatch, Match match) {

        storedMatch.setGraphData(match.getMinutes(), match.getHomeScore(), match.getAwayScore(), match.getHomeAttach(),
                match.getAwayAttach(), match.getHomeShots(), match.getAwayShots());

        Logger.err("setGraphData(); finished!");
    }

    public static void assignProperties(Match storedMatch, Match match) {
        // System.out.println(match);
        System.out.println("LOLOLOLOL " + match.getTotalGoals());
        storedMatch.assignProperties(match.getMinuteString(), match.getScore(), match.getMatchHandicap(),
                match.getTotalGoals(), match.getMatchAttach(), match.getMatchShoot());

        Logger.fine("assignProperties(); finished!");
    }

    public void printingResultSet(ResultSet rs, String nameOfResultSet) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println("Printing: " + nameOfResultSet);
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print("columnValue: " + columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }


    public int intDatabaseCall(int ID, String preparedStatement, String methodName){
        int value = -999;
        PreparedStatement prstmt = null;
        Connection connection = null;

        try {
            connection = getConnection();
            prstmt = connection.prepareStatement(preparedStatement);
            prstmt.setInt(1, ID);
            ResultSet rs= prstmt.executeQuery(); // http://stackoverflow.com/questions/1905607/cannot-issue-data-manipulation-statements-with-executequery
            printingResultSet(rs, preparedStatement);
            value = getFirstResultSetItemInt(rs);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closePrstmt(prstmt);
            closeConnection(connection);
        }

        return value;
    }

    public double doubleDatabaseCall(int ID, String preparedStatement, String methodName){
        double value = -999;
        PreparedStatement prstmt = null;
        Connection connection = null;

        try {
            connection = getConnection();
            prstmt = connection.prepareStatement(preparedStatement);
            prstmt.setInt(1, ID);
            ResultSet rs= prstmt.executeQuery(); // http://stackoverflow.com/questions/1905607/cannot-issue-data-manipulation-statements-with-executequery
            printingResultSet(rs, preparedStatement);
            value = getFirstResultSetItemDouble(rs);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closePrstmt(prstmt);
            closeConnection(connection);
        }

        return value;
    }

    public String stringDatabaseCall(int ID, String preparedStatement, String methodName){
        String value = "-999";
        PreparedStatement prstmt = null;
        Connection connection = null;

        try {
            connection = getConnection();
            prstmt = connection.prepareStatement(preparedStatement);
            prstmt.setInt(1, ID);
            ResultSet rs= prstmt.executeQuery(); // http://stackoverflow.com/questions/1905607/cannot-issue-data-manipulation-statements-with-executequery
            printingResultSet(rs, preparedStatement);
            value = getFirstResultSetItemString(rs);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            closePrstmt(prstmt);
            closeConnection(connection);
        }

        return value;
    }

    public int getFirstResultSetItemInt(ResultSet rs) throws SQLException {
        int columnValue = -999;
        while (rs.next()) {
                columnValue = rs.getInt(1);

        }
        return columnValue;
    }

    public Double getFirstResultSetItemDouble(ResultSet rs) throws SQLException {

        double columnValue = -999.9;
        while (rs.next()) {
            String value = rs.getString(1);
            columnValue = Double.parseDouble(value);
        }
        return columnValue;
    }

    public String getFirstResultSetItemString(ResultSet rs) throws SQLException {
        String columnValue = "-999";
        while (rs.next()) {
           columnValue = rs.getString(1);
        }
        return columnValue;
    }

}
