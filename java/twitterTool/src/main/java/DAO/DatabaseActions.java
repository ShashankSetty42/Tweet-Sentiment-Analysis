
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import org.json.JSONObject;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONArray;


public class DatabaseActions {
    
    public void insertUserTweets(JSONObject userData){
        
        try{
            
            String DB_URL = "jdbc:mysql://localhost/twitter_data";
            String USER = "twitter_user";
            String PASS = "twitter";

            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            
            
            
            JSONArray array = userData.getJSONArray("data");
            for(int i = 0 ; i < array.length() ; i++){
                JSONObject tweet = array.getJSONObject(i);
                statement.addBatch(
                        "INSERT INTO USERS (USER_ID, TWEET_ID, TWEET, TWEET_TIME) VALUES ( "
                                + "'" + tweet.getString("author_id") + "',"
                                + "'" + tweet.getString("id") + "',"
                                + "'" + tweet.getString("text").replace("'", "") + "',"
                                + "'" + tweet.getString("created_at").replace("Z", "").replace("T", " ") + "')"
                                  
                );
            }
            int[] updateCounts = statement.executeBatch();
            
            System.out.println(String.valueOf(updateCounts));
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void insertQuoteTweets(JSONObject userData, String tweetId){
        
        try{
            
            String DB_URL = "jdbc:mysql://localhost/twitter_data";
            String USER = "twitter_user";
            String PASS = "twitter";
            
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            
            
            
            JSONArray array = userData.getJSONArray("data");
            for(int i = 0 ; i < array.length() ; i++){
                JSONObject tweet = array.getJSONObject(i);
                statement.addBatch(
                        "INSERT INTO QUOTE_TWEETS (TWEET_ID, TWEET) VALUES ( "
                                + "'" + tweetId + "',"
                                + "'" + tweet.getString("text").replace("'", "") + "')"
                                  
                );
            }
            int[] updateCounts = statement.executeBatch();
            
            System.out.println(String.valueOf(updateCounts.length));
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public ArrayList<String> getTweetIds(){
        
        try{
            
            ArrayList<String> userIdList = new ArrayList<String>();
            String DB_URL = "jdbc:mysql://localhost/twitter_data";
            String USER = "twitter_user";
            String PASS = "twitter";
            String QUERY = "SELECT TWEET_ID FROM USERS";
            
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            
            ResultSet rs = statement.executeQuery(QUERY);	
            while (rs.next()) {
                userIdList.add(rs.getString(1));
            }
            rs.close();
            
            return userIdList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void clearTables(){
        
        try{
            
            String DB_URL = "jdbc:mysql://localhost/twitter_data";
            String USER = "twitter_user";
            String PASS = "twitter";
            
            String QUERY = "SET FOREIGN_KEY_CHECKS=0";
            String QUERY1 = "DELETE FROM USERS";
            String QUERY2 = "DELETE FROM QUOTE_TWEETS";
            String QUERY02 = "DELETE FROM SENTIMENT_RESULTS";
            String QUERY3 = "SET FOREIGN_KEY_CHECKS=1";
            
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            
            Boolean rs = statement.execute(QUERY);
            rs = statement.execute(QUERY1);	
            rs = statement.execute(QUERY2);	
            rs = statement.execute(QUERY02);	
            rs = statement.execute(QUERY3);	
            
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
        

    
}
