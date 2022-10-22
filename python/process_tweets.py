
import pymysql
import mysql.connector
import pandas as pd
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
from sqlalchemy import create_engine
import matplotlib.pyplot as graph
import matplotlib.dates


mydb = mysql.connector.connect(
  host="localhost",
  user="twitter_user",
  password="twitter",
  database="twitter_data"
)

def query_mysql(query):
    mycursor = mydb.cursor()
    mycursor.execute(query)
    return mycursor.fetchall()
    

def insert_results(df):
    cnx = create_engine('mysql+pymysql://twitter_user:twitter@localhost/twitter_data', echo=False)
    df.to_sql(name='sentiment_results', con=cnx, if_exists = 'replace', index=False)
    


def sentiment_vader(sentence):
    sid_obj = SentimentIntensityAnalyzer()
    sentiment_dict = sid_obj.polarity_scores(sentence)
    return max(sentiment_dict['neg'],sentiment_dict['neu'],sentiment_dict['pos'])


def get_tweet_ids():
    return query_mysql('SELECT DISTINCT TWEET_ID, TWEET_TIME FROM USERS')

def compute_data():
    tweets = []

    for row in get_tweet_ids(): 

        
        sentiment_values = []
        tweet_id = row[0]
        tweet_time = row[1]
        tweets_query = """
        SELECT T.TWEET
        FROM USERS U
        INNER JOIN QUOTE_TWEETS T
        ON U.TWEET_ID = T.TWEET_ID
        WHERE U.TWEET_ID = '{id}'
        """.format(id=tweet_id)
        for tweet_rows in query_mysql(tweets_query):
            sentiment_values.append(sentiment_vader(tweet_rows[0]))
        
        print(sentiment_values)
        

        if not sentiment_values:
            continue
        average = sum(sentiment_values) / len(sentiment_values)
        print('average : ' + str(average))
        tweets.append({
            'tweet_id' : tweet_id, 
            'tweet_time': tweet_time,
            'sentiment' : round(average,4) 
            })

    df = pd.DataFrame(tweets, columns=['tweet_id', 'tweet_time','sentiment'])
    insert_results(df)

def plot_graph():
    time_values = []
    sentiment_values = []
    for tweet_rows in query_mysql('SELECT TWEET_TIME, SENTIMENT FROM SENTIMENT_RESULTS'):
        time, value = tweet_rows
        time_values.append(time)
        sentiment_values.append(value)
      
    #time_values =  matplotlib.dates.date2num(time_values)
    graph.plot(time_values,sentiment_values)
    graph.gcf().autofmt_xdate()
    graph.xlabel("time")  # add X-axis label
    graph.ylabel("sentiment")  # add Y-axis label
    graph.title("elon musk")  # add title
    graph.show()


def main():
    #compute_data()
    plot_graph()

if __name__ == "__main__":
    main()