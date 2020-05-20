package com.venkat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.venkat.model.City;

public class CityDao implements ICityDao {

	@Override
	public List<City> findAll() {

		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriver(new org.h2.Driver());
		ds.setUrl("jdbc:h2:mem:testdb");

		List<City> cities = new ArrayList<>();

		String query = "SELECT * FROM cities;";

		try {
			JdbcTemplate jtm = new JdbcTemplate(ds);
			cities = jtm.query(query, new BeanPropertyRowMapper(City.class));
		} catch (DataAccessException dae) {
			Logger lgr = Logger.getLogger(CityDao.class.getName());
			lgr.log(Level.SEVERE, dae.getMessage(), dae);
		}

		return cities;
	}

	@Override
	public boolean save(City city) {

		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriver(new org.h2.Driver());
		ds.setUrl("jdbc:h2:mem:testdb");

		String sql = "INSERT INTO cities(name, population) VALUES (?, ?)";

		boolean ret = true;

		try {
			JdbcTemplate jtm = new JdbcTemplate(ds);
			jtm.update(sql, new Object[] { city.getName(), city.getPopulation() });

		} catch (DataAccessException dae) {
			Logger lgr = Logger.getLogger(CityDao.class.getName());
			lgr.log(Level.SEVERE, dae.getMessage(), dae);

			ret = false;
		}

		return ret;
	}

	@Override
	public City find(Long id) {

		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriver(new org.h2.Driver());
		ds.setUrl("jdbc:h2:mem:testdb");

		String sql = "SELECT * FROM cities WHERE Id=?";

		City city = new City();

		try {
			JdbcTemplate jtm = new JdbcTemplate(ds);
			city = (City) jtm.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper(City.class));
		} catch (DataAccessException dae) {
			Logger lgr = Logger.getLogger(CityDao.class.getName());
			lgr.log(Level.SEVERE, dae.getMessage(), dae);
		}

		return city;
	}

	@Override
	public boolean update(City city, Long id) {

		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriver(new org.h2.Driver());
		ds.setUrl("jdbc:h2:mem:testdb");

		boolean ret = true;

		String sql = "UPDATE cities SET name=?, population=? WHERE Id=?";

		try {
			JdbcTemplate jtm = new JdbcTemplate(ds);
			int nOfRows = jtm.update(sql, new Object[] { city.getName(), city.getPopulation(), id });
			if (nOfRows != 1) {
				ret = false;
			}
		} catch (DataAccessException dae) {
			Logger lgr = Logger.getLogger(CityDao.class.getName());
			lgr.log(Level.SEVERE, dae.getMessage(), dae);
			ret = false;
		}

		return ret;
	}

	@Override
	public boolean delete(Long id) {

		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		ds.setDriver(new org.h2.Driver());
		ds.setUrl("jdbc:h2:mem:testdb");

		boolean ret = true;

		String sql = "DELETE FROM cities WHERE Id=?";
		try {
			JdbcTemplate jtm = new JdbcTemplate(ds);
			int nOfRows = jtm.update(sql, new Object[] { id });
			if (nOfRows != 1) {
				ret = false;
			}
		} catch (DataAccessException dae) {
			Logger lgr = Logger.getLogger(CityDao.class.getName());
			lgr.log(Level.SEVERE, dae.getMessage(), dae);
			ret = false;
		}

		return ret;
	}
}
