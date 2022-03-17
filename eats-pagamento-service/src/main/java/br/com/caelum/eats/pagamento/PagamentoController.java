package br.com.caelum.eats.pagamento;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/pagamentos")
@AllArgsConstructor
class PagamentoController {

	private static final Logger LOG = LoggerFactory.getLogger(PagamentoController.class);
	
	private PagamentoRepository pagamentoRepo;
	
	@Autowired
	private MonolitoClient monolitoClient;

	@GetMapping
	ResponseEntity<List<PagamentoDto>> lista() {
		LOG.info("Listagem de pagamentos será realizada");
		
		ResponseEntity<List<PagamentoDto>> listaPagamentos = ResponseEntity.ok(pagamentoRepo.findAll()
				.stream()
				.map(PagamentoDto::new)
				.collect(Collectors.toList()));
		
		LOG.info("Listagem de pagamentos realizada com sucesso");
		return listaPagamentos;
	}

	@GetMapping("/{id}")
	PagamentoDto detalha(@PathVariable("id") Long id) {
		LOG.info("Detalhe do pagamento com id {} será realizada ", id);
		
		PagamentoDto pagamento = pagamentoRepo.findById(id)
				.map(PagamentoDto::new)
				.orElseThrow(ResourceNotFoundException::new);
		
		LOG.info("Detalhe do pagamento com id {} realizada com sucesso", id);
		
		return pagamento;
	}

	@PostMapping
	ResponseEntity<PagamentoDto> cria(@RequestBody Pagamento pagamento, UriComponentsBuilder uriBuilder) {
		LOG.info("Criação de pagamento do pedido: {} será realizada", pagamento.getPedidoId());
		pagamento.setStatus(Pagamento.Status.CRIADO);
		Pagamento salvo = pagamentoRepo.save(pagamento);
		URI path = uriBuilder.path("/pagamentos/{id}").buildAndExpand(salvo.getId()).toUri();
		
		LOG.info("Criação de pagamento do pedido: {} realizada com sucesso", pagamento.getPedidoId());
		return ResponseEntity.created(path).body(new PagamentoDto(salvo));
	}

	@PutMapping("/{id}")
	@HystrixCommand(fallbackMethod = "confirmaFallback")
	PagamentoDto confirma(@PathVariable("id") Long id) {
		LOG.info("Confirmação de pagamento de id: {} será realizada", id);
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
		pagamento.setStatus(Pagamento.Status.CONFIRMADO);
		
		PedidoMudancaDeStatusRequest pedidoMudanca = new PedidoMudancaDeStatusRequest("pago".toUpperCase());
		
		monolitoClient.atualizaStatusPedido(pagamento.getPedidoId(), pedidoMudanca);
		pagamentoRepo.save(pagamento);
		LOG.info("Confirmação de pagamento do pedido: {} realizada com sucesso", pagamento.getPedidoId());
		return new PagamentoDto(pagamento);
	}

	@DeleteMapping("/{id}")
	PagamentoDto cancela(@PathVariable("id") Long id) {
		LOG.info("Requisição para cancelamento de pagamento de id: {} será realizada", id);
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
		pagamento.setStatus(Pagamento.Status.CANCELADO);
		pagamentoRepo.save(pagamento);
		LOG.info("Requisição para cancelamento de pagamento de id: {} realizada com sucesso", id);
		return new PagamentoDto(pagamento);
	}
	
	PagamentoDto confirmaFallback(@PathVariable("id") Long id) {
		LOG.info("Aconteceu uma falha ao atualizar o status do pedido");
		Pagamento pagamento = pagamentoRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
		pagamento.setStatus(Pagamento.Status.PROCESSANDO);
				
		pagamentoRepo.save(pagamento);
		LOG.info("Alteração do pedido para processando realizado com sucesso", pagamento.getPedidoId());
		return new PagamentoDto(pagamento);
	}

}