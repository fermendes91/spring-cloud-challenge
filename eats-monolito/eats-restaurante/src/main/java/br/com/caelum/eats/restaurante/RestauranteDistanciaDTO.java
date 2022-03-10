package br.com.caelum.eats.restaurante;

public class RestauranteDistanciaDTO {

	private Long id;

	private String cep;

	private Long tipoDeCozinhaId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Long getTipoDeCozinhaId() {
		return tipoDeCozinhaId;
	}

	public void setTipoDeCozinhaId(Long tipoDeCozinhaId) {
		this.tipoDeCozinhaId = tipoDeCozinhaId;
	}
	
	public static RestauranteDistanciaDTO mapFromRestaurente(Restaurante restaurante) {
		RestauranteDistanciaDTO restaurenteDistancia = new RestauranteDistanciaDTO();
		
		restaurenteDistancia.setId(restaurante.getId());
		restaurenteDistancia.setCep(restaurante.getCep());
		restaurenteDistancia.setTipoDeCozinhaId(restaurante.getTipoDeCozinha().getId());
		
		return restaurenteDistancia;
	}
	
	public static RestauranteDistanciaDTO mapFromRestaurente(RestauranteDto restaurante) {
		RestauranteDistanciaDTO restaurenteDistancia = new RestauranteDistanciaDTO();
		
		restaurenteDistancia.setId(restaurante.getId());
		restaurenteDistancia.setCep(restaurante.getCep());
		restaurenteDistancia.setTipoDeCozinhaId(restaurante.getTipoDeCozinha().getId());
		
		return restaurenteDistancia;
	}
}
