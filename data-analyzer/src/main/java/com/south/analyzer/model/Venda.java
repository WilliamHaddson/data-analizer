package com.south.analyzer.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Venda
 *
 * @author William
 *
 */
@Getter
@Setter
public class Venda {

	/**
	 * ID padrão de Vendas
	 */
	@Getter
	private static final Long ID = 003L;

	
	/**
	 * Id da venda
	 */
	private Long vendaId;
	
	/**
	 * Nome do vendedor
	 */
	private String nomeVendedor;

	
	/**
	 * Lista de itens da venda
	 */
	private List<Item> itens;

}
