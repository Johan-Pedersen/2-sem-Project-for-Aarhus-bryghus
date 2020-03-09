package model;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.util.function.BiConsumer;

public interface Observer {

	public void update(PriceList pl, Object o, BiConsumer<PriceListLine, Object> setFunction);
}
