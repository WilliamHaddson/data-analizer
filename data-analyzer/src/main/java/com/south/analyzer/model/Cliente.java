package com.south.analyzer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente {

	@Getter
	private static final Long ID = 002L;
	
	private String cnpj;
	private String nome;
	private String areaTrabalho;

}
