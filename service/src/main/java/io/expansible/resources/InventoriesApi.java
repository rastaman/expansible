package io.expansible.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;
import io.expansible.model.Inventory;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.*;

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

    public Inventory create(Inventory i);

    public List<Inventory> readAll();

    public Optional<Inventory> readById(Long id);

  }

  public InventoriesApi(IInventoriesApiService inventoriesService) {
    super();
    this.inventoriesService = inventoriesService;
  }

  @GET
  @ApiOperation(value = "", notes = "Get all the registered inventories\n", response = Inventory.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful response"),
      @ApiResponse(code = 503, message = "Unexpected error") })
  @Timed
  @UnitOfWork
  @Path("/type/{type}")
  public Response inventoriesGet(@ApiParam(value = "Type of inventories") @PathParam("type") String type)
      throws NotFoundException {
    try {
      return Response.ok().entity(inventoriesService.inventoriesGet(type)).build();
    } catch (Exception e) {
      return Response.serverError().entity(e.getMessage()).build();
    }
  }

  @POST
  @Timed
  @UnitOfWork
  @Consumes(APPLICATION_JSON)
  @ApiOperation(value = "Create Inventory", response = Inventory.class)
  @ApiResponses(value = { @ApiResponse(code = 422, message = "Inventory already exists") })
  public Response createInventory(@Valid Inventory inventory) {
    Inventory created = inventoriesService.create(inventory);
    return status(CREATED).entity(created).build();
  }

  @GET
  @Timed
  @UnitOfWork
  @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
  @ApiOperation(value = "Get all Inventory", response = List.class)
  @Path("/all")
  public List<Inventory> getAllInventory() {
    return inventoriesService.readAll();
  }

  @GET
  @Path("/{id}")
  @Timed
  @UnitOfWork
  @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
  @ApiOperation(value = "Get Inventory by Id", response = Inventory.class)
  @ApiResponses(value = { @ApiResponse(code = 404, message = "Inventory not found") })
  public Optional<Inventory> getInventoryById(@ApiParam(required = true) @PathParam("id") final Long id) {
    return inventoriesService.readById(id);
  }
}
