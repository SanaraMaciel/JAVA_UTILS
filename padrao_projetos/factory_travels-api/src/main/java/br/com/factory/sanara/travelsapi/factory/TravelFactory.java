package br.com.factory.sanara.travelsapi.factory;

import br.com.factory.sanara.travelsapi.model.Travel;

/**
 * Interface that provides method for manipulate an object Travel.
 * 
 * @author Mariana Azevedo
 * @since 08/09/2019
 */
public interface TravelFactory {

	Travel createTravel (String type);
}
