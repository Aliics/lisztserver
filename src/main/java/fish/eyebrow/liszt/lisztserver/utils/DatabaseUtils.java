package fish.eyebrow.liszt.lisztserver.utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseUtils {
	public static Connection connectToDatabase( String dbUrl, String dbName, String configPath ) {
		Connection connection = null;

		try {
			Properties config = new Properties();
			config.load( new FileReader( configPath ) );

			String dbUsername = ( String ) config.get( "db_username" );
			String dbPassword = ( String ) config.get( "db_password" );

			connection = DriverManager.getConnection( dbUrl + dbName, dbUsername, dbPassword );
		} catch ( SQLException | IOException e ) {
			e.printStackTrace();
		}

		return connection;
	}

	public static Connection connectToDatabase( String configPath ) {
		Connection connection = null;

		try {
			Properties config = new Properties();
			config.load( new FileReader( configPath ) );

			String dbUrl = ( String ) config.get( "db_url" );
			String dbName = ( String ) config.get( "db_name" );

			connection = connectToDatabase( dbUrl, dbName, configPath );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		return connection;
	}

	public static void closeQuietly( Connection connection ) {
		try {
			if ( connection != null )
				connection.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	public static void closeQuietly( Statement statement ) {
		try {
			if ( statement != null )
				statement.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	public static void closeQuietly( ResultSet resultSet ) {
		try {
			if ( resultSet != null )
				resultSet.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}
}
