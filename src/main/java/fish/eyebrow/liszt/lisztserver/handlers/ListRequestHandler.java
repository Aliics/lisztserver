package fish.eyebrow.liszt.lisztserver.handlers;

import fish.eyebrow.liszt.lisztserver.DatabaseConnection;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			String searchForId = "SELECT * FROM lists WHERE id='" + parsedListId + "'";

			Connection connection = DatabaseConnection.connectToDatabase( "liszt_data", "webapps/config.properties" );
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery( searchForId );

			if ( resultSet.next() )
				wasListFound = true;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		JSONObject jsonRequestReply = new JSONObject();
		jsonRequestReply.put( "passed-request", listId );
		jsonRequestReply.put( "parsed-request", parsedListId );
		jsonRequestReply.put( "parsed-request-found", wasListFound );

		return jsonRequestReply.toString();
	}
}
