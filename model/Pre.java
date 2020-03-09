package model;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

public class Pre {
	public static void require(boolean precondition) {
		if (!precondition)
			throw new RuntimeException("Pre condition violated");
	}
}
