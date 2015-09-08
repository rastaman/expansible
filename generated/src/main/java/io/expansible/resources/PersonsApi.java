package io.expansible.resources;

import io.expansible.model.*;

import com.wordnik.swagger.annotations.ApiParam;

import io.expansible.model.ErrorModel;
import io.expansible.model.Person;

import java.util.List;
import io.expansible.resources.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

import com.codahale.metrics.annotation.Timed;

@Path("/persons")
@com.wordnik.swagger.annotations.Api(value = "/persons", description = "the persons API")
public class PersonsApi {

  private IPersonsApiService personsService;

  public IPersonsApiService getPersonsApiService() {
    return personsService;
  }

  public void setPersonsApiService(IPersonsApiService personsService) {
    this.personsService = personsService;
  }

  public interface IPersonsApiService {

  
  public List<Person> personsGet(Double size);
  

  }

  public PersonsApi(IPersonsApiService personsService) {
    super();
    this.personsService = personsService;
  }

  
  @GET
  
  
  
  @com.wordnik.swagger.annotations.ApiOperation(value = "", notes = "Gets `Person` objects.\nOptional query param of **size** determines\nsize of returned array\n", response = Person.class, responseContainer = "List")
  @com.wordnik.swagger.annotations.ApiResponses(value = { 
    @com.wordnik.swagger.annotations.ApiResponse(code = 200, message = "Successful response"),
    
    @com.wordnik.swagger.annotations.ApiResponse(code = 0, message = "Unexpected error") })
  @Timed
  public Response personsGet(@ApiParam(value = "Size of array",required=true) @QueryParam("size") Double size)
      throws NotFoundException {
    try {
      return Response.ok().entity(personsService.personsGet(size)).build();
    } catch ( Exception e ) {
      return Response.serverError().entity(e.getMessage()).build();
    }
  }

  
}
