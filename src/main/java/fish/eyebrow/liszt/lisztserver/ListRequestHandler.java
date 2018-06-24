package fish.eyebrow.liszt.lisztserver;

import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path( "/request/list" )
public class ListRequestHandler {
	@GET
	@Path( "/{listId}" )
	@Produces( MediaType.APPLICATION_JSON )
	public String clientRequestListReply( @PathParam( "listId" ) String listId ) {
		String parsedListId = listId.split( " " )[ 0 ];

		JSONObject jsonRequestReply = new JSONObject();
		jsonRequestReply.put( "passed-request", listId );
		jsonRequestReply.put( "parsed-request", parsedListId );

		return jsonRequestReply.toString();
	}
}
