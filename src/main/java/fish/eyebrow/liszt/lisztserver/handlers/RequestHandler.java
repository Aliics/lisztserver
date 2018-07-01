package fish.eyebrow.liszt.lisztserver.handlers;

import fish.eyebrow.liszt.lisztserver.utils.DatabaseUtils;
import fish.eyebrow.liszt.lisztserver.types.RequestType;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path( "/request/{requestType}" )
@SuppressWarnings( "unchecked" )
public class RequestHandler {
	@GET
	@Path( "/{param}" )
	@Produces( MediaType.APPLICATION_JSON )
	public String requestWithParamReply( @PathParam( "requestType" ) String requestType, @PathParam( "param" ) String param ) {
		String parsedRequestType = requestType;
		String parsedParam = param.split( " " )[ 0 ];
		String dataRetrieved = null;
		boolean updateSuccess = false;
		boolean deletionSuccess = false;

		try {
			Class.forName( "com.mysql.jdbc.Driver" );
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
		}

		switch ( requestType ) {
			case RequestType.LIST_RETRIEVE:
				String retrieveAllFromListData = "SELECT content FROM lists WHERE id='" + parsedParam + "'";

				Connection connection = DatabaseUtils.connectToDatabase( "/var/lib/tomcat8/webapps/config.properties" );
				ResultSet resultSet = DatabaseUtils.generateResultSetFromSqlAndConnection( connection, retrieveAllFromListData );

				try {
					if ( resultSet.next() )
						dataRetrieved = resultSet.getString( "content" );
				} catch ( SQLException e ) {
					e.printStackTrace();
				} finally {
					DatabaseUtils.closeQuietly( resultSet );
					DatabaseUtils.closeQuietly( connection );
				}

				break;
			case RequestType.LIST_UPDATE:
				updateSuccess = true;
				break;
			case RequestType.LIST_DELETE:
				deletionSuccess = true;
				break;
			default:
				parsedRequestType = null;
				parsedParam = null;
				break;
		}

		JSONObject jsonRequestReply = new JSONObject();
		jsonRequestReply.put( "client-request-type", requestType );
		jsonRequestReply.put( "client-param-passed", param );
		jsonRequestReply.put( "parsed-request-type", parsedRequestType );
		jsonRequestReply.put( "parsed-param", parsedParam );
		jsonRequestReply.put( "data-retrieved-from-params", dataRetrieved );
		jsonRequestReply.put( "data-update-success", updateSuccess );
		jsonRequestReply.put( "data-delete-success", deletionSuccess );

		return jsonRequestReply.toString();
	}
}
