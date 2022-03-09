package br.com.caelum.eats.pedido;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
class PedidoController {

	public static final Logger LOG = LoggerFactory.getLogger(PedidoController.class);
	
	private PedidoRepository repo;

	@GetMapping("/pedidos")
	List<PedidoDto> lista() {
		LOG.info("Request da listagem de pedidos será executada");
		List<PedidoDto> pedidos = repo.findAll().stream().map(PedidoDto::new).collect(Collectors.toList()); 
		LOG.info("Request de listagem realizada com sucesso");
		return pedidos;
	}

	@GetMapping("/pedidos/{id}")
	PedidoDto porId(@PathVariable("id") Long id) {
		LOG.info("Request do detalhe de um pedido {} será executada", id);
		Pedido pedido = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
		LOG.info("Request do detalhamento do pedido {} realizado com sucesso", id);
		return new PedidoDto(pedido);
	}

	@PostMapping("/pedidos")
	PedidoDto adiciona(@RequestBody Pedido pedido) {
		LOG.info("Request para a criação de um pedido será executada");
		pedido.setDataHora(LocalDateTime.now());
		pedido.setStatus(Pedido.Status.REALIZADO);
		pedido.getItens().forEach(item -> item.setPedido(pedido));
		pedido.getEntrega().setPedido(pedido);
		Pedido salvo = repo.save(pedido);
		LOG.info("Request para a criação realizada com sucesso, id: ", salvo.getId());
		return new PedidoDto(salvo);
	}

	@PutMapping("/pedidos/{pedidoId}/status")
	PedidoDto atualizaStatus(@PathVariable Long pedidoId, @RequestBody Pedido pedidoParaAtualizar) {
		LOG.info("Request para a atualização do status do pedido, {} será executada", pedidoId);
		Pedido pedido = repo.porIdComItens(pedidoId).orElseThrow(ResourceNotFoundException::new);
		pedido.setStatus(pedidoParaAtualizar.getStatus());
		repo.atualizaStatus(pedido.getStatus(), pedido);
		LOG.info("Request para a atualização do status do pedido, {} realizada com sucesso", pedidoId);
		return new PedidoDto(pedido);
	}

	@PutMapping("/pedidos/{id}/pago")
	void pago(@PathVariable("id") Long id) {
		LOG.info("Request para a atualização do status do pedido {} para pago será executada", id);
		Pedido pedido = repo.porIdComItens(id).orElseThrow(ResourceNotFoundException::new);
		pedido.setStatus(Pedido.Status.PAGO);
		repo.atualizaStatus(Pedido.Status.PAGO, pedido);
		LOG.info("Request para a atualização do status do pedido {} realizada com sucesso", id);
	}

	@GetMapping("/parceiros/restaurantes/{restauranteId}/pedidos/pendentes")
	List<PedidoDto> pendentes(@PathVariable("restauranteId") Long restauranteId) {
		LOG.info("Request para listagem de pedidos do restaurante {} pendentes será executada", restauranteId);
		
		List<PedidoDto> pedidosPendentes = repo
				.doRestauranteSemOsStatus(restauranteId, Arrays.asList(Pedido.Status.REALIZADO, Pedido.Status.ENTREGUE))
				.stream().map(pedido -> new PedidoDto(pedido)).collect(Collectors.toList());
		
		LOG.info("Request para listagem de pedidos do restaurante {} realizada com sucesso", restauranteId);
		return pedidosPendentes;
	}

}
