package model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente {
	private String cpf;
	private String nome;
	private String email;
	private String limite_de_credito;
	private String dt_nascimento;
	
	@Override
	public String toString() {
		return cpf +  nome +  email + limite_de_credito + dt_nascimento;
	}

	
	
	
	
	

}
