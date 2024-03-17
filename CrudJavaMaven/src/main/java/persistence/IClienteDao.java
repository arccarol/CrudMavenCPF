package persistence;

import model.Cliente;
import java.sql.SQLException;

public interface IClienteDao {
	
	public String iudCliente(String acao, Cliente c) throws SQLException, ClassNotFoundException;

}