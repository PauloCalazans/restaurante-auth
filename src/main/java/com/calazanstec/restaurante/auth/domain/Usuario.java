package com.calazanstec.restaurante.auth.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


/**
 * @author Paulo Calazans on 11/01/2025
 */

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Usuario {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String senha;

}
