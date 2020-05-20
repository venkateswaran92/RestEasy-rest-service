package com.venkat.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.venkat.conf.filter.JWTTokenNeeded;
import com.venkat.exception.NotFoundException;
import com.venkat.model.City;
import com.venkat.service.ICityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("cities")
@Api(value = "CityResource")
public class CityResource {

	@Inject
	private ICityService cityService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded
	@ApiOperation(value = "get cities")
	public Response getCities() throws NotFoundException {
		List<City> cities = cityService.findAll();
		if (!cities.isEmpty()) {
			return Response.ok(cities).build();
		} else {
			throw new NotFoundException(404, "city not found");
		}
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded
	@ApiOperation(value = "findCity")
	public Response findCity(@ApiParam(value = "id") @PathParam("id") Long id) throws NotFoundException {
		City city = cityService.find(id);

		if (city.getId() != null) {
			return Response.ok(city).build();
		} else {
			throw new NotFoundException(404, "Id: " + id + " city not found");
		}
	}

	@POST
	@JWTTokenNeeded
	@ApiOperation(value = "saveCity")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response saveCity(@ApiParam(value = "name") @FormParam("name") String name,
			@ApiParam(value = "population") @FormParam("population") int population) {
		City city = new City();
		city.setName(name);
		city.setPopulation(population);
		boolean r = cityService.save(city);
		if (r) {
			return Response.ok().status(Response.Status.CREATED).build();
		} else {
			return Response.notModified().build();
		}
	}

	@Path("/{id}")
	@PUT
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@ApiOperation(value = "updateCity")
	public Response updateCity(@ApiParam(value = "name") @FormParam("name") String name,
			@ApiParam(value = "population") @FormParam("population") int population, @PathParam("id") Long id) {
		City city = new City();
		city.setName(name);
		city.setPopulation(population);
		boolean r = cityService.update(city, id);
		if (r) {
			return Response.ok().status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.notModified().build();
		}
	}

	@Path("/{id}")
	@DELETE
	@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "deleteCity")
	public Response deleteCity(@ApiParam(value = "id") @PathParam("id") Long id) {
		boolean r = cityService.delete(id);
		if (r) {
			return Response.ok().status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.notModified().build();
		}
	}
}