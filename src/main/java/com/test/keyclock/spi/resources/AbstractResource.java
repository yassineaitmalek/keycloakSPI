package com.test.keyclock.spi.resources;


import com.test.keyclock.spi.dto.ApiDataResponse;
import java.util.function.Supplier;
import javax.ws.rs.core.Response;


public interface AbstractResource {
	public default <T> Response ok(Supplier<T> supplier) {
		return Response.ok(
		        ApiDataResponse.<T>builder().data(supplier.get()).httpStatus(Response.Status.OK.getStatusCode()).status(Response.Status.OK.toString()).build()
		).build();
	}

	public default <T> Response created(Supplier<T> supplier) {
		return Response.accepted().status(Response.Status.CREATED).entity(
		        ApiDataResponse.<T>builder().data(supplier.get()).httpStatus(Response.Status.CREATED.getStatusCode()).status(Response.Status.CREATED.toString()).build()
		).build();
	}

	public default Response noContent(Runnable runnable) {
		runnable.run();
		return Response.noContent().build();
	}

	public default <T> Response async(Runnable runnable, Supplier<T> supplier) {
		runnable.run();
		return Response.accepted().status(Response.Status.ACCEPTED).entity(
		        ApiDataResponse.<T>builder().data(supplier.get()).httpStatus(Response.Status.ACCEPTED.getStatusCode()).status(Response.Status.ACCEPTED.toString()).build()
		).build();
	}

}
