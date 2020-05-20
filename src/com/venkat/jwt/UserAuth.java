package com.venkat.jwt;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/users")
@Api(value = "userAuth")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class UserAuth {

	@Context
	private UriInfo uriInfo;

	@Inject
	private Logger logger;

	@Inject
	private KeyGenerator keyGenerator;

	@POST
	@Path("/login")
	@ApiOperation(value = "login")
	@Consumes(APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(@ApiParam(value = "login") @FormParam("login") String login,
			@ApiParam(value = "password") @FormParam("password") String password) {
		try {
			logger.info("login/password : " + login + "/" + password);
			// Authenticate the user using the credentials provided
			if (authenticate(login, password)) {
				// Issue a token for the user
				String token = issueToken(login);
				// Return the token on the response
				return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();
			}
		} catch (Exception e) {
			return Response.status(UNAUTHORIZED).build();
		}
		return Response.status(UNAUTHORIZED).build();
	}

	private boolean authenticate(String login, String password) throws Exception {
		return (login.equals("admin") && password.equals("admin"));
	}

	private String issueToken(String login) {
		Key key = keyGenerator.generateKey();
		String jwtToken = Jwts.builder().setSubject(login).setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
				.signWith(SignatureAlgorithm.HS512, key).compact();
		logger.info("generating token for a key : " + jwtToken + " - " + key);
		return jwtToken;
	}

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
}
