package br.com.sanara.builderCarros.director;

import br.com.sanara.builderCarros.classes.Engine;
import br.com.sanara.builderCarros.classes.GPSNavigator;
import br.com.sanara.builderCarros.classes.TripComputer;
import br.com.sanara.builderCarros.enums.CarType;
import br.com.sanara.builderCarros.enums.Transmission;
import br.com.sanara.builderCarros.interfaces.Builder;

public class Director {
	public void constructSportsCar(Builder builder) {
		builder.setCarType(CarType.SPORTS_CAR);
		builder.setSeats(2);
		builder.setEngine(new Engine(3.0, 0));
		builder.setTransmission(Transmission.SEMI_AUTOMATIC);
		builder.setTripComputer(new TripComputer());
		builder.setGPSNavigator(new GPSNavigator());
	}

	public void constructCityCar(Builder builder) {
		builder.setCarType(CarType.CITY_CAR);
		builder.setSeats(2);
		builder.setEngine(new Engine(1.2, 0));
		builder.setTransmission(Transmission.AUTOMATIC);
		builder.setTripComputer(new TripComputer());
		builder.setGPSNavigator(new GPSNavigator());
	}

	public void constructSUV(Builder builder) {
		builder.setCarType(CarType.SUV);
		builder.setSeats(4);
		builder.setEngine(new Engine(2.5, 0));
		builder.setTransmission(Transmission.MANUAL);
		builder.setGPSNavigator(new GPSNavigator());
	}
}
