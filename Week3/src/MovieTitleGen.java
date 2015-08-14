import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class MovieTitleGen
{
	public static Connection conn = null;
	public static void main(String[] args) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		openConnection();
		String sql = "SELECT * FROM (SELECT * FROM movie_gen ORDER BY DBMS_RANDOM.RANDOM) WHERE rownum =1";
		ResultSet result = getFromDB(sql);
		String adjective = "";
		String noun="";
		while(result.next()){
			 adjective = result.getString("adjectives");
			 noun = result.getString("nouns");
		}
		System.out.println("Myxyllplyk's Random Movie Title Generator\n");

		System.out.println( "Your movie title is: " + adjective + " " + noun );
		System.out.println("Please include a description for your movie");
		String description = sc.nextLine();
		sql = "Insert into movie_titles values('"+adjective+"','"+noun+"','"+description+"')";
		updateDB(sql);
	}	

	/**
	*	@param url - the URL to read words from
	*	@return An array of words, initialized from the given URL
	*/	
	public static String[] arrayFromUrl( String url ) throws Exception
	{
		Scanner fin = new Scanner((new URL(url)).openStream());
		int count = fin.nextInt();

		String[] words = new String[count];

		for ( int i=0; i<words.length; i++ )
		{
			words[i] = fin.next();
		}
		fin.close();

		return words;
	}
    //properties for creating connection to Oracle database
	
	 
   public static void openConnection(){
		String url = "jdbc:oracle:thin:testuser/password@localhost"; 
   	 Properties props = new Properties();
   	 props.setProperty("user", "testdb");
        props.setProperty("password", "password");
        try {
			conn = DriverManager.getConnection(url,props);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
   
	public static void updateDB(String sql)throws SQLException {
	        //URL of Oracle database server
	       

	        //String sql ="select cust_last_name,cust_city,cust_state from demo_customers";

	        //creating PreparedStatement object to execute query
	        PreparedStatement preStatement = conn.prepareStatement(sql);
	    
	        preStatement.setQueryTimeout(10);
	        preStatement.executeUpdate(); 
	       
	      
	    }
	
   public static ResultSet getFromDB(String sql)throws SQLException {
      
       PreparedStatement preStatement = conn.prepareStatement(sql);
       ResultSet result = preStatement.executeQuery(); 
       return result;
     
   }
}