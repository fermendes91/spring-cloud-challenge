package br.com.caelum.eats.restaurante;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("distancia-service")
public interface DistanciaClient {
	
	@PostMapping("/restaurantes")
	public void adiciona(@RequestBody RestauranteDistanciaDTO restaurante);
	
	@PutMapping("/restaurantes/{id}")
	public void atualiza(@PathVariable("id") Long id, @RequestBody RestauranteDistanciaDTO restaurante);
	
}
