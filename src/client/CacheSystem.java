package client;

import client.resolvers.CachedCommand;

import java.util.HashMap;
import java.util.Map;

public class CacheSystem {

	/**
	 * Clase singleton(sólo una instancia) que gestiona la entrada y actualización de elementos
	 * cacheados.
	 */

	public static CacheSystem instance = new CacheSystem();
	/**
	 * la clave, que debe ser única, es un string formato por el tipo de recurso
	 * consultado (A, AAAA, NS...) y el host a consultar (uvigo.es, nasa.gov...)
	 * La clave apunta a un objeto resolvedor (TCPChannel o UDPChannel), que hemos
	 * clonado y metido en otra clase para gestionarlo en torno a la cache
	 */

	private Map<String, CachedCommand> cache;

	private CacheSystem() {
		// Almacenaremos la cache en un mapa
		cache = new HashMap<>();
	}

	public static CacheSystem getInstance() {
		return instance;
	}

	/**
	 * Sirve para saber si en la caché existe un consulta que ya se ha lanzado previamente
	 *
	 * @param queryArgs
	 * @return
	 */
	public boolean has(QueryArgs queryArgs) {
		return cache.containsKey(queryArgs.getKey());
	}

	/**
	 * Obtiene un objeto de cacheado mediante un objeto QueryArgs que almacena
	 * la consulta parseada obtenida por teclado
	 *
	 * @param queryArgs
	 * @return
	 */
	public CachedCommand get(QueryArgs queryArgs) {
		return cache.get(queryArgs.getKey());
	}

	/**
	 * Introduce en la caché una respuesta
	 *
	 * @param queryArgs
	 * @param cachedCommand
	 */

	public void put(QueryArgs queryArgs, CachedCommand cachedCommand) {
		cache.put(queryArgs.getKey(), cachedCommand);
	}

	/**
	 * Recorre todos los elementos de la cache y elimina los que hayan expirado
	 */
	public void update() {
		cache.entrySet().removeIf(pair -> pair.getValue().hasExpired());
	}


	/**
	 * Interfaz que define métodos que cualquier objeto susceptible de ser cacheado debería tener
	 */

	public interface Cacheable {

		boolean hasExpired();

		int getTL();
	}

}
