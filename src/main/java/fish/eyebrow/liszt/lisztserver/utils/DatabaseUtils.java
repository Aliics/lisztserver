package fish.eyebrow.liszt.lisztserver.utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseUtils {
	public static ResultSet generateResultSetFromSqlAndConnection( Connection connection, String sql ) {
		ResultSet resultSet = null;

		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery( sql );
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultSet;
	}

	public static Connection connectToDatabase( String url, String database, String configPath ) {
		Connection connection = null;

		try {
			Class.forName( "com.mysql.jdbc.Driver" );

			Properties config = new Properties();
			config.load( new FileReader( configPath ) );

			String username = ( String ) config.get( "db_username" );
			String password = ( String ) config.get( "db_password" );

			connection = DriverManager.getConnection( url + database, username, password );
		} catch ( ClassNotFoundException | SQLException | IOException e ) {
			e.printStackTrace();
		}

		return connection;
	}

	public static Connection connectToDatabase( String configPath ) {
		Connection connection = null;

		try {
			Properties config = new Properties();
			config.load( new FileReader( configPath ) );

			String url = ( String ) config.get( "db_url" );
			String database = ( String ) config.get( "db_name" );

			connection = connectToDatabase( url, database, configPath );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		return connection;
	}

	public static void closeQuietly( Connection connection ) {
		try {
			connection.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	public static void closeQuietly( ResultSet resultSet ) {
		try {
			resultSet.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

}
