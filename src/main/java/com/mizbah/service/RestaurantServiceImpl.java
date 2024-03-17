package com.mizbah.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mizbah.adapter.RestaurantAdapter;
import com.mizbah.dto.RestaurantDto;
import com.mizbah.entity.Restaurant;
import com.mizbah.entity.User;
import com.mizbah.exception.DependencyException;
import com.mizbah.repository.RestaurantRepository;
import com.mizbah.repository.UserRepository;
import com.mizbah.service.interfaces.RestaurantService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

	RestaurantRepository restaurantRepository;
	UserRepository userRepository;

	RestaurantAdapter restaurantAdapter;

	@Override
	public List<RestaurantDto> getAllRestaurants() {
		return restaurantAdapter.toDto(restaurantRepository.findAll());
	}

	@Override
	public RestaurantDto getRestaurantById(long id) {
		Optional<Restaurant> restaurant = restaurantRepository.findById(id);
		if (restaurant.isEmpty()) {
			throw new EntityNotFoundException("Restaurant not found with ID: " + id);
		}
		return restaurantAdapter.toDto(restaurant.get());
	}

	@Override
	public List<RestaurantDto> getRestaurantByName(String name) {
		List<Restaurant> restaurants = restaurantRepository.findByNameContaining(name);
		return restaurantAdapter.toDto(restaurants);
	}

	@Override
	public RestaurantDto createRestaurant(RestaurantDto restaurantRequest) {

		Optional<User> user = userRepository.findById(restaurantRequest.getOwnerId());

		if (user.isEmpty()) {
			throw new DependencyException("Owner not found with Id: " + restaurantRequest.getOwnerId());
		}

		Restaurant restaurant = restaurantAdapter.toEntity(restaurantRequest);
		restaurant.setOwner(user.get());

		restaurantRepository.save(restaurant);

		return restaurantAdapter.toDto(restaurant);
	}

	@Override
	@Transactional
	public RestaurantDto updateRestaurant(long id, RestaurantDto restaurantRequest) {

		Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
				"Restaurant not found with ID: " + id));

		// No Owner change allowed
		restaurant.setName(restaurantRequest.getName());
		restaurant.setId(id);

		restaurant = restaurantRepository.save(restaurant);

		return restaurantAdapter.toDto(restaurant);

	}

	@Transactional
	@Override
	public void deleteRestaurant(long id) {
		if (!restaurantRepository.existsById(id)) {
			throw new EntityNotFoundException("Restaurant not found with ID: " + id);
		}
		// TODO
		// CASCADE to branches? Yes
		// CASCADE to tables? allow? yes
		// CASCADE to bookings? allow? yes, with notify
		restaurantRepository.deleteById(id);

	}

}
