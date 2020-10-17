package com.south.analyzer.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Vendedor
 * 
 *  @author William
 *
 */
@Getter
@Setter
public class Vendedor {

	
	/**
	 * ID padrão de Vendedores
	 */
	@Getter
	private static final Long ID = 001L;

	
	/**
	 * CPF do vendedor
	 */
	private String cpf;
	
	/**
	 * Nome do vendedor
	 */
	private String nome;
	
	/**
	 * Salário do vendedor
	 */
	private Double salario;
	
	
	/**
	 * Quantidade de vendas realizadas por um vendedor
	 */
	private Integer quantidadeVendas;

}
