
package com.shashank.twittertool;
import org.json.JSONObject;
import utils.ApiConnection;
import DAO.DatabaseActions;

public class TwitterTool {
    
    private static String BEARER_TOKEN = "<your token here>";
    public static void main(String[] args)  {
        
        String profileId = "44196397";
        DatabaseActions dbo = new DatabaseActions();
        String url = "https://api.twitter.com/2/users/" + profileId + "/tweets?tweet.fields=author_id%2Ccreated_at&exclude=replies%2Cretweets&max_results=75";
        
        ApiConnection obj = new ApiConnection();
        String response = "";
        try{
            response = obj.getResponse(BEARER_TOKEN, url);
        }catch(Exception ex){
            ex.printStackTrace(System.out);
        }
        JSONObject responseJSON = new JSONObject(response.toString());
        System.out.println(responseJSON);
        
        dbo.clearTables();
        dbo.insertUserTweets(responseJSON);
        
        for(String tweetId : dbo.getTweetIds()){
            System.out.println("Checking retweets for : " + tweetId);
            String quoteTweetResponse = "";
            String tweetUrl = "https://api.twitter.com/2/tweets/" + tweetId + "/quote_tweets?max_results=75&user.fields=created_at";
            System.out.println(tweetUrl);
            try{
                quoteTweetResponse = obj.getResponse(BEARER_TOKEN, tweetUrl);
            }catch(Exception ex){
                ex.printStackTrace(System.out);
            }
            JSONObject quoteTweetResponseJSON = new JSONObject(quoteTweetResponse.toString());
            dbo.insertQuoteTweets(quoteTweetResponseJSON, tweetId);
            try{
                System.out.println("sleeping for 10s");
                Thread.sleep(10000);
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
}
