package br.com.factory.sanara.factory.impl;

import br.com.factory.sanara.travelsapi.enumeration.TravelTypeEnum;
import br.com.factory.sanara.travelsapi.factory.TravelFactory;
import br.com.factory.sanara.travelsapi.model.Travel;

/**
 * Factory class for the Travel entity.
 * 
 * @author Mariana Azevedo
 * @since 08/09/2019
 */
public class TravelFactoryImpl implements TravelFactory {

	@Override
	public Travel createTravel (String type) {
		
		if (TravelTypeEnum.ONE_WAY.getValue().equals(type)) {
			return new Travel(TravelTypeEnum.ONE_WAY);
		} else if (TravelTypeEnum.MULTI_CITY.getValue().equals(type)) {
			return new Travel(TravelTypeEnum.MULTI_CITY); 
		}
		
		return new Travel(TravelTypeEnum.RETURN);
	}

}
