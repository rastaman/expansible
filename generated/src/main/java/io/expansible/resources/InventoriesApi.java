package io.expansible.resources;

import io.expansible.model.*;

import com.wordnik.swagger.annotations.ApiParam;

import io.expansible.model.ErrorModel;
import io.expansible.model.Inventory;

import java.util.List;
import io.expansible.resources.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

import com.codahale.metrics.annotation.Timed;

@Path("/inventories")
@com.wordnik.swagger.annotations.Api(value = "/inventories", description = "the inventories API")
public class InventoriesApi {

  private IInventoriesApiService inventoriesService;

  public IInventoriesApiService getInventoriesApiService() {
    return inventoriesService;
  }

  public void setInventoriesApiService(IInventoriesApiService inventoriesService) {
    this.inventoriesService = inventoriesService;
  }

  public interface IInventoriesApiService {

  
  public List<Inventory> inventoriesGet(String type);
  

  }

  public InventoriesApi(IInventoriesApiService inventoriesService) {
    super();
    this.inventoriesService = inventoriesService;
  }

  
  @GET
  
  
  
  @com.wordnik.swagger.annotations.ApiOperation(value = "", notes = "Get all the registered inventories\n", response = Inventory.class, responseContainer = "List")
  @com.wordnik.swagger.annotations.ApiResponses(value = { 
    @com.wordnik.swagger.annotations.ApiResponse(code = 200, message = "Successful response"),
    
    @com.wordnik.swagger.annotations.ApiResponse(code = 0, message = "Unexpected error") })
  @Timed
  public Response inventoriesGet(@ApiParam(value = "Type of inventories") @QueryParam("type") String type)
      throws NotFoundException {
    try {
      return Response.ok().entity(inventoriesService.inventoriesGet(type)).build();
    } catch ( Exception e ) {
      return Response.serverError().entity(e.getMessage()).build();
    }
  }

  
}
