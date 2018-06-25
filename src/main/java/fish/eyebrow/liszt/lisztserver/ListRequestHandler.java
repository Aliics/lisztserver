package fish.eyebrow.liszt.lisztserver;

import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

@Path( "/request/list" )
@SuppressWarnings( "unchecked" )
public class ListRequestHandler {
	@GET
	@Path( "/{listId}" )
	@Produces( MediaType.APPLICATION_JSON )
	public String clientRequestListReply( @PathParam( "listId" ) String listId ) {
		String parsedListId = listId.split( " " )[ 0 ];
		boolean wasListFound = false;

		try {
			Class.forName( "com.mysql.jdbc.Driver" );

			String searchForId = "SELECT * FROM lists WHERE id='" + parsedListId + "'";

			Connection connection = connectToDatabase( "liszt_data", "webapps/config.properties" );
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery( searchForId );

			if ( resultSet.next() )
				wasListFound = true;
		} catch ( ClassNotFoundException | SQLException e ) {
			e.printStackTrace();
		}

		JSONObject jsonRequestReply = new JSONObject();
		jsonRequestReply.put( "passed-request", listId );
		jsonRequestReply.put( "parsed-request", parsedListId );
		jsonRequestReply.put( "parsed-request-found", wasListFound );

		return jsonRequestReply.toString();
	}

	private Connection connectToDatabase( String database, String configPath ) {
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
