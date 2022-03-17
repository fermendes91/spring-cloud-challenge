package br.com.caelum.eats.pagamento;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Getter;

@FeignClient("monolito")
public interface MonolitoClient {

	@PutMapping(value="pedidos/{pedidoId}/status")
	public void atualizaStatusPedido(@PathVariable("pedidoId") Long pedidoId, @RequestBody PedidoMudancaDeStatusRequest pedido);

}

@Getter
@AllArgsConstructor
class PedidoMudancaDeStatusRequest {
    private String status;
}
