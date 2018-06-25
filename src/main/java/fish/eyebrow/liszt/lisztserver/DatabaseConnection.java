package fish.eyebrow.liszt.lisztserver;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
	public static Connection connectToDatabase( String database, String configPath ) {
		Connection connection = null;

		try {
			Class.forName( "com.mysql.jdbc.Driver" );

			Properties config = new Properties();
			config.load( new FileReader( configPath ) );

			String username = ( String ) config.get( "db_username" );
			String password = ( String ) config.get( "db_password" );

			connection = DriverManager.getConnection( "jdbc:mysql://localhost/" + database, username, password );
		} catch ( ClassNotFoundException | SQLException | IOException e ) {
			e.printStackTrace();
		}

		return connection;
	}
}
