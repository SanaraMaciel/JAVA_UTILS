package br.com.sanara.builderCarros.interfaces;

import br.com.sanara.builderCarros.classes.Engine;
import br.com.sanara.builderCarros.classes.GPSNavigator;
import br.com.sanara.builderCarros.classes.TripComputer;
import br.com.sanara.builderCarros.enums.CarType;
import br.com.sanara.builderCarros.enums.Transmission;

public interface Builder {
	void setCarType(CarType type);

	void setSeats(int seats);

	void setEngine(Engine engine);

	void setTransmission(Transmission transmission);

	void setTripComputer(TripComputer tripComputer);

	void setGPSNavigator(GPSNavigator gpsNavigator);
}