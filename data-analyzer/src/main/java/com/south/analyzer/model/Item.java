package com.south.analyzer.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Item de uma venda
 * 
 * @author William
 * 
 */
@Getter
@Setter
public class Item {
	
	/**
	 * Id do item
	 */
	private Long id;
	
	/**
	 * Quandtidade do item, em uma venda
	 */
	private Integer quantidade;
	
	/**
	 * Preço do item
	 */
	private Double preco;

}
