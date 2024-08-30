package br.com.sanara.builderCarros.builders;

import br.com.sanara.builderCarros.classes.Engine;
import br.com.sanara.builderCarros.classes.GPSNavigator;
import br.com.sanara.builderCarros.classes.Manual;
import br.com.sanara.builderCarros.classes.TripComputer;
import br.com.sanara.builderCarros.enums.CarType;
import br.com.sanara.builderCarros.enums.Transmission;
import br.com.sanara.builderCarros.interfaces.Builder;

public class CarManualBuilder implements Builder {
	private CarType type;
	private int seats;
	private Engine engine;
	private Transmission transmission;
	private TripComputer tripComputer;
	private GPSNavigator gpsNavigator;

	@Override
	public void setCarType(CarType type) {
		this.type = type;
	}

	@Override
	public void setSeats(int seats) {
		this.seats = seats;
	}

	@Override
	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}

	@Override
	public void setTripComputer(TripComputer tripComputer) {
		this.tripComputer = tripComputer;
	}

	@Override
	public void setGPSNavigator(GPSNavigator gpsNavigator) {
		this.gpsNavigator = gpsNavigator;
	}

	public Manual getResult() {
		return new Manual(type, seats, engine, transmission, tripComputer, gpsNavigator);
	}
}
