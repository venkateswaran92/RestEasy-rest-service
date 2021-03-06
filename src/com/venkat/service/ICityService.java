package com.venkat.service;

import java.util.List;

import com.venkat.model.City;

public interface ICityService {

	public List<City> findAll();

	public boolean save(City city);

	public City find(Long id);

	public boolean update(City city, Long id);

	public boolean delete(Long id);
}