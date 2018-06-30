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
		String parsedParam = param;

		JSONObject jsonRequestReply = new JSONObject();
		jsonRequestReply.put( "client-request-type", requestType );
		jsonRequestReply.put( "client-param-passed", param );

		switch ( requestType ) {
			case RequestType.LIST_RETRIEVE:
				parsedParam = param.split( " " )[ 0 ];

				String jsonParamKey = "data-retrieved-from-params";

				String retrieveAllFromListData = "SELECT content FROM lists WHERE id='" + parsedParam + "'";

				Connection connection = DatabaseUtils.connectToDatabase( "webapps/config.properties" );
				ResultSet resultSet = DatabaseUtils.generateResultSetFromSqlAndConnection( connection,  retrieveAllFromListData);

				try {
					if (resultSet.next())
						jsonRequestReply.put( jsonParamKey, resultSet.getString( "content" ) );
				} catch ( SQLException e ) {
					e.printStackTrace();
				} finally {
					DatabaseUtils.closeQuietly( connection );
					DatabaseUtils.closeQuietly( resultSet );

					if ( jsonRequestReply.get( jsonParamKey ) == null )
						jsonRequestReply.put( jsonParamKey, null ); // SEEMS REDUNDANT, BUT IT IS NECESSARY
				}
				break;
			default:
				jsonRequestReply.put( "client-request-type", "404" );
				break;
		}

		jsonRequestReply.put( "param-parsed-by-server", parsedParam );

		return jsonRequestReply.toString();
	}
}
